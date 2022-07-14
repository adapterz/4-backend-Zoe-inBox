package inbox.inbox.portfolio;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 인증 실패(유저 정보 불일치 or 인증번호 불일치)
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class PortfolioConfirmUnauthorizedException extends RuntimeException {
    public PortfolioConfirmUnauthorizedException() {
    }
}
