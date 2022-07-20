package inbox.inbox.cookie;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 응답 메세지 객체
@Getter
@NoArgsConstructor
public class CookieResponseMessage {
    private String message;
    @Builder
    CookieResponseMessage(String message){
        this.message = message;
    }
}
