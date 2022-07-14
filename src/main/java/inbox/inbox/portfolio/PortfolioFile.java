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
import lombok.Setter;

// file 테이블에 매핑해줄 엔티티
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "file")
public class PortfolioFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long file_idx;
    @Column(columnDefinition = "CHAR(64)")
    private String file_name;
    @Column(columnDefinition = "CHAR(9)")
    private String extension;
    @Column(name = "created_at", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    public Timestamp created_at;


    @Builder
    public PortfolioFile(String fileName, String extension) {
        this.file_name = fileName;
        this.extension = extension;
    }

}
