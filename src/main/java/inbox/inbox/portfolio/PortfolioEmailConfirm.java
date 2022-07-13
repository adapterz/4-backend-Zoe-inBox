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

// Confirm 테이블
@Getter
@NoArgsConstructor
@Entity
@Table(name = "confirm")
public class PortfolioEmailConfirm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long confirm_idx;
    private Integer confirm_code;
    @Column(nullable=false,length=100)
    private String email;
    @Column(columnDefinition="CHAR(15)")
    private String ip;
    @Column(columnDefinition="CHAR(64)")
    private String user_agent;
    @Column(name = "created_at", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    public Timestamp created_at;

    @Builder
    public PortfolioEmailConfirm(Integer confirm_code, String email, String ip,String user_agent) {
        this.confirm_code = confirm_code;
        this.email = email;
        this.ip = ip;
        this.user_agent = user_agent;
    }
}
