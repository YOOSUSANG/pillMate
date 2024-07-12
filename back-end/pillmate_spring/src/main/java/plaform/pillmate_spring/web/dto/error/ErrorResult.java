package plaform.pillmate_spring.web.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResult {
    private String errorMessage;
    private HttpStatus errorCode;

}
