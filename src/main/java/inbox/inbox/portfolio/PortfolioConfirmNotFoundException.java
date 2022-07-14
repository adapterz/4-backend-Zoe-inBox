package inbox.inbox.portfolio;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// confirm 테이블에 요청한 인덱스의 정보가 없을 때
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PortfolioConfirmNotFoundException extends RuntimeException{

    public PortfolioConfirmNotFoundException() {
    }
}
