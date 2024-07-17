package plaform.pillmate_spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Generics API")
@AllArgsConstructor
@NoArgsConstructor
public class JsonResult<T> {
    @Schema(description = "T 타입으로 변환",defaultValue = "List가 들어올 시에 [] -> {[]}")
    private T data;
}
