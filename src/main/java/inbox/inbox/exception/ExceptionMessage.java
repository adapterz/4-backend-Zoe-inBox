package inbox.inbox.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

// 예외 메세지 클래스
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionMessage {
    private String message;
}
