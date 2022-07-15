package inbox.inbox.portfolio;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// Confirm 테이블에 매핑해줄 엔티티
@Getter
@NoArgsConstructor
@Entity
@Table(name = "confirm")
public class PortfolioConfirm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long confirm_idx;
    private Integer confirm_code;
    @Column(nullable = false, length = 100)
    private String email;
    @Column(columnDefinition = "CHAR(39)")
    private String ip;
    @Column(columnDefinition = "CHAR(64)")
    private String user_agent_digest;
    @Column(name = "created_at", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    public Timestamp created_at;
    @Builder
    public PortfolioConfirm(Integer confirmCode, String email, String ip,
        String userAgentDigest) {
        this.confirm_code = confirmCode;
        this.email = email;
        this.ip = ip;
        this.user_agent_digest = userAgentDigest;
    }
}
