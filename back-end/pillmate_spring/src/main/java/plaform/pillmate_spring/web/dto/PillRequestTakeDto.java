package plaform.pillmate_spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Schema(description = "약품 복용 DTO")
@NoArgsConstructor
public class PillRequestTakeDto {
    @Schema(description = "약품 이름",defaultValue = "ex) 사리돈에이정 250mg/PTP, 원더칼-디츄어블정")
    private String name;

}
