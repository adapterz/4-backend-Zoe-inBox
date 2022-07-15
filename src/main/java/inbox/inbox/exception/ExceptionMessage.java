package inbox.inbox.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 예외 메세지 클래스
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionMessage {

    private String message;
    private String error;

    @Builder
    ExceptionMessage(String message, String error) {
        this.message = message;
        this.error = error;
    }

}
