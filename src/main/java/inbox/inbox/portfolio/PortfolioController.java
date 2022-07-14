package inbox.inbox.portfolio;

import static inbox.inbox.utils.ConstantManager.BE;
import static inbox.inbox.utils.ConstantManager.FE;
import static inbox.inbox.utils.ConstantManager.OFF;
import static inbox.inbox.utils.ConstantManager.PORTFOLIO_PATH;

import inbox.inbox.exception.ValidationGroup;
import inbox.inbox.utils.ConstantManager;
import inbox.inbox.exception.ValuesAllowed;
import inbox.inbox.utils.CookieManager;
import java.security.NoSuchAlgorithmException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

// 포트폴리오 컨트롤러
@Validated
@RequiredArgsConstructor
@RequestMapping(PORTFOLIO_PATH)
@RestController
public class PortfolioController {

    private final PortfolioService service;
    private final ConstantManager constant;
    private final CookieManager cookieManager;

    // 조회할 포트폴리오 범위(be/fe) 정하기 (쿠키로 on/off 여부 체크, 유저가 off 선택할 경우 쿠키 발급)
    @GetMapping("/range/{option}")
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
            response.addCookie(cookieManager.deleteCookie(option));
        }
        // 포트폴리오 다른 range 조회 옵션이 off 일 때 해당 range 조회 옵션 off 로 못함(range 가 2갠데 둘다 off 할 수 없음)
        else if (isOtherOff) {
            throw new PortfolioRangeConflictException();
        }
        // 기존 영상 조회 옵션을 on 해놨을 때 off 로 전환 - 쿠키 생성
        else {
            response.addCookie(cookieManager.makeCookie(option, OFF, 24 * 60 * 60));
        }
        return ResponseEntity.ok().build();
    }

    // 영상 업로드 전 인증 메일 발송
    @Validated(ValidationGroup.PortfolioConfirmEmailValidationGroup.class)
    @PostMapping("/email")
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

    // 유저가 입력한 인증번호 확인 API
    @Validated(ValidationGroup.PortfolioConfirmCodeValidationGroup.class)
    @PostMapping("/code")
    public PortfolioResponseMessage confirmCode(
        @RequestBody @Valid PortfolioConfirmDto portfolioConfirmDto,
        HttpServletRequest request) throws NoSuchAlgorithmException {
        // 요청한 유저정보 일치여부 확인 및 인증번호 확인
        service.confirmForAuthentication(
            portfolioConfirmDto, request);

        return PortfolioResponseMessage.builder().message(constant.AUTHORIZED)
            .email(portfolioConfirmDto.getEmail()).build();
    }


}
