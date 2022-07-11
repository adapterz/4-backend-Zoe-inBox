package inbox.inbox.exception;

import inbox.inbox.portfolio.PortfolioConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

// 예외 핸들링
@RestControllerAdvice
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {

    // 예상치 못한 런타임 에러
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleUnexpectedException() {
        ExceptionMessage exceptionMessage = new ExceptionMessage("unexpected_error");
        return new ResponseEntity<Object>(exceptionMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 유효하지 않은 param
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleParamConstraintViolationException() {
        ExceptionMessage exceptionMessage = new ExceptionMessage("invalid_request");
        return new ResponseEntity(exceptionMessage, HttpStatus.BAD_REQUEST);
    }

    // 포트폴리오 range 의 조회 조건을 모두 off 했을 때
    @ExceptionHandler(PortfolioConflictException.class)
    public ResponseEntity<Object> handlePortfolioConflictException() {
        ExceptionMessage exceptionMessage = new ExceptionMessage("at_least_one_should_be_on");
        return new ResponseEntity<Object>(exceptionMessage, HttpStatus.CONFLICT);
    }
}
