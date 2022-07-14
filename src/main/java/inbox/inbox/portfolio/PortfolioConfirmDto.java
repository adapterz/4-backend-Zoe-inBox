package inbox.inbox.portfolio;

import inbox.inbox.exception.ValidationGroup.PortfolioConfirmValidationGroup;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// PortfolioConfirm 테이블과 관련된 데이터 옮겨줄 객체의 클래스
@Data
@NoArgsConstructor
public class PortfolioConfirmDto {

    private long confirmIdx;
    private int confirmCode;
    @NotBlank(groups = {ValidationGroup.PortfolioConfirmCodeValidationGroup.class,
        ValidationGroup.PortfolioConfirmEmailValidationGroup.class})
    @Email(groups = {ValidationGroup.PortfolioConfirmCodeValidationGroup.class,
        ValidationGroup.PortfolioConfirmEmailValidationGroup.class})
    @Size(max = 100, groups = {ValidationGroup.PortfolioConfirmCodeValidationGroup.class,
        ValidationGroup.PortfolioConfirmEmailValidationGroup.class})
    private String email;
    private String ip;
    private String userAgentDigest;

    @Builder
    PortfolioConfirmDto(long confirmIdx, int confirmCode, String email, String ip,
        String userAgentDigest) {
        this.confirmIdx = confirmIdx;
        this.confirmCode = confirmCode;
        this.email = email;
        this.ip = ip;
        this.userAgentDigest = userAgentDigest;
    }


}
