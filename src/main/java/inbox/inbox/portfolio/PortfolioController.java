package inbox.inbox.portfolio;

import static inbox.inbox.utils.ConstantManager.BE;
import static inbox.inbox.utils.ConstantManager.FE;
import static inbox.inbox.utils.ConstantManager.OFF;
import static inbox.inbox.utils.ConstantManager.PORTFOLIO_PATH;

import inbox.inbox.exception.ValidationGroup.PortfolioConfirmValidationGroup;
import inbox.inbox.exception.ValidationGroup.PortfolioValidationGroup;
import inbox.inbox.utils.ConstantManager;
import inbox.inbox.exception.ValuesAllowed;
import inbox.inbox.utils.CookieManager;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

// 포트폴리오 컨트롤러
@Validated
@RequiredArgsConstructor
@RestController
public class PortfolioController {

    private final PortfolioService service;
    private final ConstantManager constant;
    private final CookieManager cookieManager;

    // 포트폴리오 정보 업로드
    @Validated(PortfolioValidationGroup.class)
    @PostMapping( PORTFOLIO_PATH)
    public @ResponseBody ResponseEntity<Object> uploadPortfolio(
        @RequestBody @Valid PortfolioDto portfolioDto,HttpServletRequest request) throws NoSuchAlgorithmException {
        /*
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());

        BindingResult bindingResult,
        }

         */
        // 요청한 유저정보 일치여부 확인 및 인증번호 확인

        PortfolioConfirmDto portfolioConfirmDto = PortfolioConfirmDto.builder().confirmIdx(
            portfolioDto.getConfirmIdx()).confirmCode(portfolioDto.getConfirmCode()).email(
            portfolioDto.getEmail()).build();

        service.confirmForAuthentication(
            portfolioConfirmDto, request);

        // 포트폴리오 정보 db에 저장
        service.addPortfolio(portfolioDto);

        return ResponseEntity.created(URI.create(PORTFOLIO_PATH)).build();

    }

    // 포트폴리오 영상 정보 가져오기
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(PORTFOLIO_PATH + "/file")
    public PortfolioResponseMessage requestPortfolio() {
        return service.getPortfolioInfo();

    }

    // 조회할 포트폴리오 범위(be/fe) 정하기 (쿠키로 on/off 여부 체크, 유저가 off 선택할 경우 쿠키 발급)
    @GetMapping(PORTFOLIO_PATH + "/range/{option}")
    public ResponseEntity<Object> switchRange(
        @PathVariable("option") @ValuesAllowed(values = {FE, BE}) String option,
        HttpServletResponse response, HttpServletRequest request) {
        // 필요한 변수 정의
        Cookie[] cookies = cookieManager.getAllRequestCookie(request);
        boolean isOtherOff = false;
        String cookieValue = null;

        /*
         * 목적 1. param 과 일치하는 key(be/fe) 의 쿠키가 있으면 그 쿠키 값 cookieValue 변수에 저장
         * 목적 2. param 과 다른 범위의 포트폴리오 조회 여부(ex. param 이 fe 라면 be 범위 조회 조건이 off 인지 여부) 체크
         * 참고 사항: 유저가 요청한 헤더에 쿠키 정보가 있을 때만 체크
         */
        if (cookies != null) {
            for (Cookie tempCookie : cookies) {
                String tempCookieName = tempCookie.getName().toString();
                // 목적 2
                if ((Objects.equals(option, BE) && Objects.equals(tempCookieName,
                    FE)) || (
                    Objects.equals(option, FE) && Objects.equals(tempCookieName,
                        BE))) {
                    String otherOptionSwitch = tempCookie.getValue().toString();
                    if (Objects.equals(otherOptionSwitch, OFF)) {
                        isOtherOff = true;
                    }
                }
                // 목적 1
                if (Objects.equals(tempCookieName, option)) {
                    cookieValue = tempCookie.getValue();
                }
            }
        }

        // 기존 포트폴리오 조회 조건이 off 일 때 쿠키 삭제 - 쿠키가 없는게 on 상태
        if (Objects.equals(cookieValue, OFF)) {
            //response.addCookie(cookieManager.deleteCookie(option));
            response.addHeader("Set-Cookie", cookieManager.deleteCookie(option).toString());
        }
        // 포트폴리오 다른 range 조회 옵션이 off 일 때 해당 range 조회 옵션 off 로 못함(range 가 2갠데 둘다 off 할 수 없음)
        else if (isOtherOff) {
            throw new PortfolioRangeConflictException();
        }
        // 기존 영상 조회 옵션을 on 해놨을 때 off 로 전환 - 쿠키 생성
        else {
            //response.addCookie(cookieManager.makeCookie(option, OFF, 24 * 60 * 60));
            response.addHeader("Set-Cookie",
                cookieManager.makeCookie(option, OFF, 24 * 60 * 60).toString());
        }
        return ResponseEntity.ok().build();

    }

    // 영상 업로드 전 인증 메일 발송
    @Validated(PortfolioConfirmValidationGroup.class)
    @PostMapping(PORTFOLIO_PATH + "/email")
    public PortfolioResponseMessage confirmEmail(
        @RequestBody @Valid PortfolioConfirmDto portfolioConfirmDto,
        HttpServletRequest request) throws NoSuchAlgorithmException {
        // 인증 메일 보내기

        service.sendConfirmCodeForEmailAuthentication(
            portfolioConfirmDto);
        // db에 (인증번호, 유저 ip, User-Agent, email) 저장
        long confirmIdx = service.addPortfolioEmailConfirm(portfolioConfirmDto, request);
        return PortfolioResponseMessage.builder().message(constant.SEND_MAIL).confirmIdx(confirmIdx)
            .build();
    }
}
