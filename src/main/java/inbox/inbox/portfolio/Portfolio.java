package inbox.inbox.portfolio;

import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// Portfolio 테이블에 매핑해줄 엔티티
@Getter
@NoArgsConstructor
@Entity
@Table(name = "portfolio")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long portfolio_idx;
    @Column(columnDefinition = "TINYINT")
    private Byte rangeVal;
    @Column(columnDefinition = "CHAR(8)")
    private String title;
    @Column(columnDefinition = "DATE")
    private Date portfolio_date;
    @Column(columnDefinition = "CHAR(12)")
    private String about;
    @OneToOne
    @JoinColumn(name = "file_idx")
    private PortfolioFile portfolioFile;
    @Column(nullable = false, length = 100)
    private String email;
    @Column(name = "created_at", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    public Timestamp created_at;


    @Builder
    public Portfolio(Byte range, String title, Date portfolioDate, String about, String email) {
        this.rangeVal = range;
        this.title = title;
        this.portfolio_date = portfolioDate;
        this.about = about;
        this.email = email;
    }

    public void setFile(PortfolioFile portfolioFile) {
        this.portfolioFile = portfolioFile;
    }


}
