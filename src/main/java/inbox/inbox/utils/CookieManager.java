package inbox.inbox.utils;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


// 쿠키 관리자 클래스
@Component
public class CookieManager {

    // 새로운 쿠키
    public ResponseCookie makeCookie(String key, String value, int maxAge) {
        /*
        Cookie newCookie = new Cookie(key, value);
        newCookie.setMaxAge(maxAge);
        newCookie.setHttpOnly(true);
        //offCookie.setSecure(true);
        newCookie.setPath("/");

         */

        ResponseCookie newCookie = ResponseCookie.from(key, value).maxAge(maxAge).httpOnly(true)
            .path("/").build();
        //offCookie.setSecure(true);
        //sameSite("None").secure(true)
        return newCookie;
    }

    // 쿠키 삭제하기 위한 쿠키 발급
    public ResponseCookie deleteCookie(String key) {
        /*
        Cookie deleteCookie = new Cookie(key, null);
        deleteCookie.setMaxAge(0);
        deleteCookie.setHttpOnly(true);
        //deleteCookie.setSecure(true);
        deleteCookie.setPath("/");

         */
        ResponseCookie deleteCookie = ResponseCookie.from(key, null).maxAge(0).httpOnly(true)
            .path("/").build();
        //.sameSite("None").secure(true)
        return deleteCookie;
    }

    public Cookie[] getAllRequestCookie(HttpServletRequest request) {
        return request.getCookies();
    }

}
