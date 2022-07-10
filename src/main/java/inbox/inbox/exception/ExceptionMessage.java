package inbox.inbox.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

// 예외 메세지 클래스
@Data
@AllArgsConstructor
public class ExceptionMessage {
    private String message;
}
