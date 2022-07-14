package inbox.inbox.portfolio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// PortfolioEmailConfirm 테이블과 관련된 데이터 옮겨줄 객체의 클래스
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioEmailConfirmDto {

    private Integer confirmCode;
    private String email;
    private String ip;
    private String userAgentDigest;

}
