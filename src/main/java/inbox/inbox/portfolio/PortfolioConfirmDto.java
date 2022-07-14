package inbox.inbox.portfolio;

import inbox.inbox.exception.ValidationGroup;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// PortfolioConfirm 테이블과 관련된 데이터 옮겨줄 객체의 클래스
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioConfirmDto {

    private Integer confirmCode;
    @NotBlank(groups = ValidationGroup.PortfolioConfirmEmailValidationGroup.class)
    @Email(groups = ValidationGroup.PortfolioConfirmEmailValidationGroup.class)
    @Size(groups = ValidationGroup.PortfolioConfirmEmailValidationGroup.class, max = 100)
    private String email;
    private String ip;
    private String userAgentDigest;

}
