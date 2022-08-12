package inbox.inbox.filter;

import static inbox.inbox.utils.ConstantManager.FILTER_PATH;
import static inbox.inbox.utils.ConstantManager.OFF;
import static inbox.inbox.utils.ConstantManager.ON;

import inbox.inbox.utils.ConstantManager;
import inbox.inbox.exception.ValuesAllowed;
import inbox.inbox.utils.CookieManager;
import java.util.Objects;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

// 영상 위에 올릴 필터 이미지 컨트롤러
@RequiredArgsConstructor
@Validated
@RestController
public class FilterController {

    private final ConstantManager constant;
    private final CookieManager cookieManager;

    // 필터 on/off
    @GetMapping(FILTER_PATH + "/{switch}")
    public ResponseEntity<Object> switchFilterOption(
        @PathVariable("switch") @ValuesAllowed(values = {ON, OFF}) String option,
        HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = cookieManager.getAllRequestCookie(request);
        boolean isOff = false;

        if (cookies != null) {
            for (Cookie tempCookie : cookies) {
                String tempCookieName = tempCookie.getName().toString();
                String tempCookieValue = tempCookie.getValue().toString();
                if (Objects.equals(tempCookieName, constant.FILTER) && Objects.equals(
                    tempCookieValue,
                    OFF)) {
                    isOff = true;
                    break;
                }
            }
        }
        if (isOff && Objects.equals(option, ON)) {
            //response.addCookie(cookieManager.deleteCookie(constant.FILTER));
            response.addHeader("Set-Cookie",
                cookieManager.deleteCookie(constant.FILTER).toString());
        }
        if (!isOff && Objects.equals(option, OFF)) {
            response.addHeader(
                "Set-Cookie",
                cookieManager.makeCookie(constant.FILTER, OFF, 24 * 60 * 60).toString());
        }

        return ResponseEntity.ok().build();
    }

}
