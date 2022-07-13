package inbox.inbox.portfolio;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 유저에게 응답해줄 응답 메세지 객체
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PortfolioResponseMessage {

    private String message;
    private String range;
    private String title;
    private String fileName;
    private String extension;
    private String portfolioDate;
    private String about;
    private String email;
    private String createdDate;
    private long confirmIdx;

    @Builder
    PortfolioResponseMessage(String message, String range, String title, String fileName,
        String extension, String portfolioDate, String about, String email, String createdDate,
        long confirmIdx) {
        this.message = message;
        this.range = range;
        this.title = title;
        this.fileName = fileName;
        this.extension = extension;
        this.portfolioDate = portfolioDate;
        this.about = about;
        this.email = email;
        this.createdDate = createdDate;
        this.confirmIdx = confirmIdx;
    }

}
