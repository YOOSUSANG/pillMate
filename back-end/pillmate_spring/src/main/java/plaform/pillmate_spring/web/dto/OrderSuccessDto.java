package plaform.pillmate_spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import plaform.pillmate_spring.domain.entity.DeliveryStatus;
import plaform.pillmate_spring.domain.entity.OrderInfo;

@Data
@Schema(description = "주문 성공 DTO")
@AllArgsConstructor
@NoArgsConstructor
public class OrderSuccessDto {
    @Schema(description = "결제고유번호", defaultValue = "ex) 12134213-223")
    private String tid;
    @Schema(description = "주문한 상품 이름", defaultValue = "ex) 타이레놀1, 타이레놀2, 타이레놀3")
    private String itemName;
    @Schema(description = "주문한 상품 가격", defaultValue = "ex) 1000, 2000, 3000")
    private int amount;
    @Schema(description = "주문 상태", defaultValue = "ex) 배송준비, 배송시작")
    private String deliveryStatusKr;

    public OrderSuccessDto(OrderInfo orderInfo) {
        this.tid = orderInfo.getTid();
        this.itemName = orderInfo.getItemName();
        this.amount = orderInfo.getAmount();
        this.deliveryStatusKr = orderInfo.getDeliveryStatusKr();
    }
}
