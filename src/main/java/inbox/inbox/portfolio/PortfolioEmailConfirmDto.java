package inbox.inbox.portfolio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioEmailConfirmDto {
    private Integer confirm_code;
    private String email;
    private String ip;
    private String user_agent;

}
