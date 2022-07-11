package inbox.inbox.controllers;

import inbox.inbox.config.ApplicationContextConfig;
import inbox.inbox.exception.ValuesAllowed;
import inbox.inbox.utils.CookieManager;
import java.util.Objects;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class FilterController {

    @GetMapping("/filters/{switch}")
    public ResponseEntity<Object> switchFilterOption(@PathVariable("switch") @ValuesAllowed(values = {"on", "off"}) String option,
        HttpServletRequest request, HttpServletResponse response) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(
            ApplicationContextConfig.class);
        CookieManager cookieManager = applicationContext.getBean(CookieManager.class);
        Cookie[] cookies = cookieManager.getAllRequestCookie(request);
        boolean isOff = false;

        if (cookies != null) {
            for (Cookie tempCookie : cookies) {
                String tempCookieName = tempCookie.getName().toString();
                String tempCookieValue = tempCookie.getValue().toString();
                if (Objects.equals(tempCookieName, "filter") && Objects.equals(tempCookieValue,
                    "off")) {
                    isOff = true;
                    break;
                }
            }
        }
        if (isOff&&Objects.equals(option,"on")) {
            response.addCookie(cookieManager.deleteCookie("filter"));
        }
        if (!isOff&&Objects.equals(option,"off")) {
            response.addCookie(cookieManager.makeCookie("filter", "off", 24 * 60 * 60));
        }

        return ResponseEntity.ok().build();
    }

}
