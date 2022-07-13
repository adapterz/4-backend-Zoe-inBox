package inbox.inbox.portfolio;

import static inbox.inbox.config.ConstantList.OFF;
import static inbox.inbox.config.ConstantList.BE;
import static inbox.inbox.config.ConstantList.FE;
import static inbox.inbox.config.ConstantList.PORTFOLIO_PATH;

import inbox.inbox.config.ApplicationContextConfig;
import inbox.inbox.exception.ValuesAllowed;
import inbox.inbox.utils.CookieManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

// 포트폴리오 컨트롤러
@Validated
@RestController
public class PortfolioController {


    // 조회할 포트폴리오 범위(be/fe) 정하기 (쿠키로 on/off 여부 체크, 유저가 off 선택할 경우 쿠키 발급)
    @GetMapping(PORTFOLIO_PATH + "/range/{option}")
    public ResponseEntity<Object> switchRange(
        @PathVariable("option") @ValuesAllowed(values = {FE, BE}) String option,
        HttpServletResponse response, HttpServletRequest request) {
        // 쿠키 관리해줄 CookieManager bean 객체 위치 가져오고 필요한 변수 정의
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(
            ApplicationContextConfig.class);
        CookieManager cookieManager = applicationContext.getBean(CookieManager.class);
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
                if ((Objects.equals(option, BE) && Objects.equals(tempCookieName, FE)) || (
                    Objects.equals(option, FE) && Objects.equals(tempCookieName, BE))) {
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
    @PostMapping("/email")
    public String confirmEmail(@RequestBody PortfolioEmailConfirmDto portfolioEmailConfirmDto) {
        Boolean isSucceed = service.SendConfirmCodeForEmailAuthentication(portfolioEmailConfirmDto);
        if (isSucceed) {
            return portfolioEmailConfirmDto.getEmail();
        } else {
            throw new RuntimeException();
        }
    }
}
