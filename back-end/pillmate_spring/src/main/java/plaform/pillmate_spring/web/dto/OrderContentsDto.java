package plaform.pillmate_spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import plaform.pillmate_spring.domain.entity.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Schema(description = "주문 목록 DTO")
@AllArgsConstructor
@NoArgsConstructor
public class OrderContentsDto {
    @Schema(description = "주문 번호", defaultValue = "ex) 12134213-223")
    private Long id;
    @Schema(description = "주문 상태", defaultValue = "ex) 배송준비, 배송시작")
    private String deliveryStatusKr;
    @Schema(description = "주문 날짜", defaultValue = "ex) 2024.07.24")
    private LocalDateTime orderTime;
    @Schema(description = "총 주문 상품", defaultValue = "ex)상품1, 상품2, 상품3")
    private List<BasketPillItemsDto> orderItems;

    public OrderContentsDto(Order order) {
        this.id = order.getId();
        this.deliveryStatusKr = order.getDelivery().getDeliveryStatus().getDescription();
        this.orderTime = order.getCreateDate();
        this.orderItems = order.getBasket().getBasketPillItems().stream().map((items) -> new BasketPillItemsDto(items)).collect(Collectors.toList());

    }
}
