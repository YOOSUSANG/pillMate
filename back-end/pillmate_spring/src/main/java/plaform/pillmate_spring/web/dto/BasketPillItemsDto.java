package plaform.pillmate_spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import plaform.pillmate_spring.domain.entity.BasketPillItem;

@Data
@Schema(description = "장바구니 상품 DTO")
@AllArgsConstructor
@NoArgsConstructor
public class BasketPillItemsDto {

    @Schema(description = "상품 고유 식별자", defaultValue = "ex) 1, 2, 3, 4")
    private Long basketPillItemId;
    @Schema(description = "상품 이름", defaultValue = "ex) 타이레놀1, 타이레놀2, 타이레놀3")
    private String name;

    @Schema(description = "상품 가격", defaultValue = "ex) 1000, 2000, 3000")
    private int OrderItemPrice;

    @Schema(description = "상품 수량", defaultValue = "ex) 1, 2, 3")
    private int quantity;

    @Schema(description = "상품 이미지 URL")
    private String imgUrl;

    public BasketPillItemsDto(BasketPillItem basketPillItem) {
        this.basketPillItemId = basketPillItem.getId();
        this.name = basketPillItem.getPill().getName();
        this.OrderItemPrice = basketPillItem.getPrice();
        this.quantity = basketPillItem.getSelectQuantity();
        this.imgUrl = basketPillItem.getPill().getImgKey();
    }
}
