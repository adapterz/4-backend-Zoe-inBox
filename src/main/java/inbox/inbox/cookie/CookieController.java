package inbox.inbox.cookie;

import static inbox.inbox.utils.ConstantManager.BE;
import static inbox.inbox.utils.ConstantManager.COOKIE_PATH;
import static inbox.inbox.utils.ConstantManager.FE;

import inbox.inbox.utils.ConstantManager;
import inbox.inbox.utils.CookieManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

// SameSite 가 아닐 때 Set-Cookie 헤더를 프론트 브라우저에 보내줘도 바로 쿠키가 생기지 않는 문제를 해결하기 위한 컨트롤러
// 다음 fetch get 요청에 그 전 요청에 대한 쿠키가 보내지는 현상 발견
@RequiredArgsConstructor
@RestController
public class CookieController {

    private final ConstantManager constant;
    private final CookieManager cookieManager;

    // 백엔드 서버 브라우저의 쿠키 가져오기
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(COOKIE_PATH)
    public CookieResponseMessage getCookieForPreviousRequest() {
        return CookieResponseMessage.builder().message(constant.GET_COOKIE).build();
    }
}
