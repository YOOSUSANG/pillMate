package plaform.pillmate_spring.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PillRequestData {
    private String name;
    private String classNo;
    private String imgKey;
    private String dlMaterial;
    private String dlMaterialEn;
    private String dlCustomShape;
    private String colorClass1;
    private String drugShape;

}
