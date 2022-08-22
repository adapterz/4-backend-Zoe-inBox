package inbox.inbox.logging;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

// 요청/응답 로그 처리해줄 로깅 인터셉터
@Component
@Slf4j
@RequiredArgsConstructor
public class LoggingInterceptor implements HandlerInterceptor {

    // 요청 로그
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {
        // 해당 로그 id (요청과 응답 매칭시켜주는 용도)
        String uuid = UUID.randomUUID().toString();
        request.setAttribute("log_id", uuid);

        String queryString = request.getQueryString();
        log.info("{} {} {} {} {}", keyValue("log_type", "Request"),
            keyValue("method", request.getMethod()),
            keyValue("uri", queryString == null ? request.getRequestURI()
                : request.getRequestURI() + queryString), keyValue("log_id",
                request.getAttribute("log_id")),
            keyValue("user_agent", request.getHeader("User-Agent")));
        return true;
    }

    // 응답 로그
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        String queryString = request.getQueryString();

        log.info("{} {} {} {} {} {}",
            keyValue("log_type", "Response"),
            keyValue("method", request.getMethod()),
            keyValue("uri", queryString == null ? request.getRequestURI()
                : request.getRequestURI() + queryString),
            keyValue("status", response.getStatus()),
            keyValue("log_id",
                request.getAttribute("log_id")),
            keyValue("user_agent", request.getHeader("User-Agent")));
    }
}

