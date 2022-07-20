package inbox.inbox.cookie;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

// SameSite 가 아닐 때 Set-Cookie 헤더를 프론트 브라우저에 보내줘도 바로 쿠키가 생기지 않는 문제를 해결하기 위한 컨트롤러
// 다음 fetch get 요청에 그 전 요청에 대한 쿠키가 보내지는 현상 발견
@RestController
public class CookieController {
    // 백엔드 서버 브라우저의 쿠키 가져오기
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/cookie")
    public CookieResponseMessage getCookieForPreviousRequests() {
        return CookieResponseMessage.builder().message("get_cookie").build();
    }
}
