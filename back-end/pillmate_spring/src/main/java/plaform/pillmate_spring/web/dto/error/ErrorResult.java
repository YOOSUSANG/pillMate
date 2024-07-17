package plaform.pillmate_spring.web.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Schema(description = "error API")
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResult {
    @Schema(description = "에러메시지",defaultValue = "ex) 잘못된 정보가 들어왔습니다. 존재하지 않는 회원입니다.")
    private String errorMessage;
    @Schema(description = "에러코드",defaultValue = "ex) 404, 401 ...")
    private HttpStatus errorCode;

}
