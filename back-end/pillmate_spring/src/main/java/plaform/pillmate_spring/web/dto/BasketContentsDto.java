package plaform.pillmate_spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import plaform.pillmate_spring.domain.entity.Basket;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Schema(description = "장바구니 DTO")
@AllArgsConstructor
@NoArgsConstructor
public class BasketContentsDto {

    @Schema(description = "총 담은 가격", defaultValue = "ex) 30000, 20000")
    private int totalPrice;
    @Schema(description = "담은 장바구니 상품", defaultValue = "ex)상품1, 상품2, 상품3")
    private List<BasketPillItemsDto> basketItems;

    public BasketContentsDto(Basket basket) {
        this.totalPrice = basket.itemTotalPrice();
        this.basketItems = basket.getBasketPillItems().stream().map((items) -> new BasketPillItemsDto(items)).collect(Collectors.toList());

    }
}
