package inbox.inbox.portfolio;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 유저에게 응답해줄 응답 메세지 객체
@Getter
@NoArgsConstructor
@JsonInclude(Include.NON_DEFAULT)
public class PortfolioResponseMessage {

    private String message;
    private String range;
    private String title;
    private String fileName;
    private String extension;

    private String portfolioDate;
    private String about;
    private String email;
    private long confirmIdx;

    private String createdDate;

    @Builder
    PortfolioResponseMessage(String message, String range, String title, String fileName,
        String extension, Date portfolioDate, String about, String email,
        long confirmIdx, Date createdDate) {
        this.message = message;
        this.range = range;
        this.title = title;
        this.fileName = fileName;
        this.extension = extension;
        if (!(portfolioDate == null)) {
            this.portfolioDate = portfolioDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
        this.about = about;
        this.email = email;
        this.confirmIdx = confirmIdx;

        if (!(portfolioDate == null)) {
            this.createdDate = createdDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
    }

}
