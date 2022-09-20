package inbox.inbox.portfolio;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 포트폴리오 range 의 조회 조건을 모두 off 했을 때의 예외 클래스
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PortfolioRangeConflictException extends RuntimeException {

    public PortfolioRangeConflictException() {
    }
}
