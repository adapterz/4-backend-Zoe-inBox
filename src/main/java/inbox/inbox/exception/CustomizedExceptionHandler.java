package inbox.inbox.exception;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

import inbox.inbox.portfolio.PortfolioConfirmNotFoundException;
import inbox.inbox.portfolio.PortfolioConfirmUnauthorizedException;
import inbox.inbox.portfolio.PortfolioNotFoundException;
import inbox.inbox.utils.ConstantManager;
import inbox.inbox.portfolio.PortfolioRangeConflictException;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

// 예외 핸들링
@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {

    private final ConstantManager constant;

    // 예상치 못한 런타임 에러
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleUnexpectedException(HttpServletRequest request) {

        ExceptionMessage exceptionMessage = ExceptionMessage.builder()
            .message(constant.UNEXPECTED_ERROR).build();

        log.error("{} {} {} {}",
            keyValue("log_type", "Exception"),
            keyValue("exception_type", "RuntimeReception"),
            keyValue("status", HttpStatus.INTERNAL_SERVER_ERROR.toString().substring(0, 3)),
            keyValue("log_id", request.getAttribute("log_id")));

        return new ResponseEntity<Object>(exceptionMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 유효하지 않음
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(Exception exception,
        HttpServletRequest request) {

        String exceptionString = exception.getMessage();
        String[] exceptionArray = exceptionString.split(":|,");

        ExceptionMessage exceptionMessage = ExceptionMessage.builder()
            .message(constant.INVALID_REQUEST).error(exceptionArray[1].substring(1)).build();

        log.info("{} {} {} {} {}",
            keyValue("log_type", "Exception"),
            keyValue("exception_type", "ConstraintViolationException"),
            keyValue("status", HttpStatus.BAD_REQUEST.toString().substring(0, 3)),
            keyValue("invalid_key", exceptionArray[1].substring(1)),
            keyValue("log_id", request.getAttribute("log_id")));

        return new ResponseEntity(exceptionMessage, HttpStatus.BAD_REQUEST);
    }

    // 포트폴리오 range 의 조회 조건을 모두 off 했을 때
    @ExceptionHandler(PortfolioRangeConflictException.class)
    public ResponseEntity<Object> handlePortfolioConflictException(HttpServletRequest request) {

        ExceptionMessage exceptionMessage = ExceptionMessage.builder()
            .message(constant.AT_LEAST_ONE_SHOULD_BE_ON).build();

        log.info("{} {} {} {} ",
            keyValue("log_type", "Exception"),
            keyValue("exception_type", "PortfolioRangeConflictException"),
            keyValue("status", HttpStatus.CONFLICT.toString().substring(0, 3)),
            keyValue("log_id", request.getAttribute("log_id")));

        return new ResponseEntity<Object>(exceptionMessage, HttpStatus.CONFLICT);
    }

    // confirm 테이블에 일치하는 idx 정보가 없을 때
    @ExceptionHandler(PortfolioConfirmNotFoundException.class)
    public ResponseEntity<Object> handlePortfolioConfirmNotFoundException(
        HttpServletRequest request) {

        ExceptionMessage exceptionMessage = ExceptionMessage.builder()
            .message(constant.NOT_FOUND).build();

        log.info("{} {} {} {} ",
            keyValue("log_type", "Exception"),
            keyValue("exception_type", "PortfolioConfirmNotFoundException"),
            keyValue("status", HttpStatus.NOT_FOUND.toString().substring(0, 3)),
            keyValue("log_id", request.getAttribute("log_id")));

        return new ResponseEntity<Object>(exceptionMessage, HttpStatus.NOT_FOUND);
    }

    // confirm 테이블의 정보와 인증 요청한 유저의 정보 or 인증번호가 일치하지 않을 때
    @ExceptionHandler(PortfolioConfirmUnauthorizedException.class)
    public ResponseEntity<Object> handlePortfolioConfirmUnauthorizedException(
        HttpServletRequest request) {

        ExceptionMessage exceptionMessage = ExceptionMessage.builder()
            .message(constant.AUTH_FAIL).build();

        log.info("{} {} {} {} ",
            keyValue("log_type", "Exception"),
            keyValue("exception_type", "PortfolioConfirmUnauthorizedException"),
            keyValue("status", HttpStatus.UNAUTHORIZED.toString().substring(0, 3)),
            keyValue("log_id", request.getAttribute("log_id")));

        return new ResponseEntity<Object>(exceptionMessage, HttpStatus.UNAUTHORIZED);
    }

    // 유저의 요청은 올바르지만 응답해줄 포트폴리오 정보가 없을 때
    @ExceptionHandler(PortfolioNotFoundException.class)
    public ResponseEntity<Object> handlePortfolioNotFoundException(HttpServletRequest request) {

        ExceptionMessage exceptionMessage = ExceptionMessage.builder()
            .message(constant.NOT_EXIST).build();

        log.info("{} {} {} {} ",
            keyValue("log_type", "Exception"),
            keyValue("exception_type", "PortfolioNotFoundException"),
            keyValue("status", HttpStatus.OK.toString().substring(0, 3)),
            keyValue("log_id", request.getAttribute("log_id")));

        return new ResponseEntity<Object>(exceptionMessage, HttpStatus.OK);
    }

    // post 요청 시 payload 의 json 을 객체로 mapping 할 때 type 매칭이 안되는 예외 핸들러
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        HttpMessageNotReadableException exception, HttpHeaders headers, HttpStatus httpStatus,
        WebRequest request) {

        String exceptionString = exception.getMostSpecificCause().toString();

        String[] exceptionArray = exceptionString.split("\"");
        int arrayLen = exceptionArray.length;
        ExceptionMessage exceptionMessage = ExceptionMessage.builder()
            .message(constant.INVALID_REQUEST).error(exceptionArray[arrayLen - 2]).build();

        log.info("{} {} {} {} ",
            keyValue("log_type", "Exception"),
            keyValue("exception_type", "HttpMessageNotReadable"),
            keyValue("invalid_key", exceptionArray[arrayLen - 2]),
            keyValue("status", HttpStatus.BAD_REQUEST.toString().substring(0, 3)));

        return new ResponseEntity(exceptionMessage, HttpStatus.BAD_REQUEST);
    }
}
