package inbox.inbox.middlewares;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


// 쿠키 관리자 클래스
@Component
public class CookieManager {

    // 새로운 쿠키
    public Cookie makeCookie(String key, String value, int maxAge) {
        Cookie newCookie = new Cookie(key, value);
        newCookie.setMaxAge(maxAge);
        newCookie.setHttpOnly(true);
        //offCookie.setSecure(true);
        newCookie.setPath("/");
        return newCookie;
    }

    // 쿠키 삭제하기 위한 쿠키 발급
    public Cookie deleteCookie(String key) {
        Cookie deleteCookie = new Cookie(key, null);
        deleteCookie.setMaxAge(0);
        deleteCookie.setHttpOnly(true);
        //deleteCookie.setSecure(true);
        deleteCookie.setPath("/");
        return deleteCookie;
    }

    public Cookie[] getAllRequestCookie(HttpServletRequest request) {
        return request.getCookies();
    }

}
