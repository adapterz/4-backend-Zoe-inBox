package inbox.inbox.exception;

import inbox.inbox.utils.ConstantManager;
import inbox.inbox.portfolio.PortfolioRangeConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

// 예외 핸들링
@RequiredArgsConstructor
@RestControllerAdvice
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {

    private final ConstantManager constant;

    // 예상치 못한 런타임 에러
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleUnexpectedException() {
        ExceptionMessage exceptionMessage = new ExceptionMessage(constant.UNEXPECTED_ERROR);
        return new ResponseEntity<Object>(exceptionMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 유효하지 않은 param
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleParamConstraintViolationException() {
        ExceptionMessage exceptionMessage = new ExceptionMessage(constant.INVALID_REQUEST);
        return new ResponseEntity(exceptionMessage, HttpStatus.BAD_REQUEST);
    }

    // 포트폴리오 range 의 조회 조건을 모두 off 했을 때
    @ExceptionHandler(PortfolioRangeConflictException.class)
    public ResponseEntity<Object> handlePortfolioConflictException() {
        ExceptionMessage exceptionMessage = new ExceptionMessage(
            constant.AT_LEAST_ONE_SHOULD_BE_ON);
        return new ResponseEntity<Object>(exceptionMessage, HttpStatus.CONFLICT);
    }
}
