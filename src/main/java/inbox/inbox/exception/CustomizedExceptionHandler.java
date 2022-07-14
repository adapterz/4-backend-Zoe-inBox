package inbox.inbox.exception;

import inbox.inbox.portfolio.PortfolioConfirmNotFoundException;
import inbox.inbox.portfolio.PortfolioConfirmUnauthorizedException;
import inbox.inbox.utils.ConstantManager;
import inbox.inbox.portfolio.PortfolioRangeConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
        ExceptionMessage exceptionMessage = ExceptionMessage.builder()
            .message(constant.UNEXPECTED_ERROR).build();
        return new ResponseEntity<Object>(exceptionMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 유효하지 않음
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException() {
        ExceptionMessage exceptionMessage = ExceptionMessage.builder()
            .message(constant.INVALID_REQUEST).build();
        return new ResponseEntity(exceptionMessage, HttpStatus.BAD_REQUEST);
    }

    // 포트폴리오 range 의 조회 조건을 모두 off 했을 때
    @ExceptionHandler(PortfolioRangeConflictException.class)
    public ResponseEntity<Object> handlePortfolioConflictException() {
        ExceptionMessage exceptionMessage = ExceptionMessage.builder()
            .message(constant.AT_LEAST_ONE_SHOULD_BE_ON).build();
        return new ResponseEntity<Object>(exceptionMessage, HttpStatus.CONFLICT);
    }

    // confirm 테이블에 일치하는 idx 정보가 없을 때
    @ExceptionHandler(PortfolioConfirmNotFoundException.class)
    public ResponseEntity<Object> handlePortfolioConfirmNotFoundException() {
        ExceptionMessage exceptionMessage = ExceptionMessage.builder()
            .message(constant.NOT_FOUND).build();
        return new ResponseEntity<Object>(exceptionMessage, HttpStatus.NOT_FOUND);
    }

    // confirm 테이블의 정보와 인증 요청한 유저의 정보 or 인증번호가 일치하지 않을 때
    @ExceptionHandler(PortfolioConfirmUnauthorizedException.class)
    public ResponseEntity<Object> handlePortfolioConfirmUnauthorizedException() {
        ExceptionMessage exceptionMessage = ExceptionMessage.builder()
            .message(constant.AUTH_FAIL).build();
        return new ResponseEntity<Object>(exceptionMessage, HttpStatus.NOT_FOUND);
    }
}
