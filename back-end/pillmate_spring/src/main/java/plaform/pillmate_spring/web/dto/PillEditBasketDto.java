package plaform.pillmate_spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "장바구니 알약 수정 DTO")
@AllArgsConstructor
@NoArgsConstructor
public class PillEditBasketDto {
    @Schema(description = " 장바구니에 담긴 알약 ID", defaultValue = "ex) 1, 2, 3, 4 ...")
    private Long basketPillItemId;
    @Schema(description = "수정하고자 하는 알약 수량", defaultValue = "ex) 1, 2, 3, 4 ...")
    private int quantity;

}
