package inbox.inbox.portfolio;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 유저의 요청은 올바르지만 응답해줄 포트폴리오 정보가 없을 때
@ResponseStatus(HttpStatus.OK)
public class PortfolioNotFoundException extends RuntimeException {

    PortfolioNotFoundException() {
    }

}
