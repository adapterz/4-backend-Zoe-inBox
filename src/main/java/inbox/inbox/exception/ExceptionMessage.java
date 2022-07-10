package inbox.inbox.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 예외 메세지 클래스
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionMessage {
    private String message;
}
