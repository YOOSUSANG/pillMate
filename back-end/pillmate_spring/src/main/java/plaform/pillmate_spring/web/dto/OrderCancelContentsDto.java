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
@Schema(description = "주문 취소 목록 DTO")
@AllArgsConstructor
@NoArgsConstructor
public class OrderCancelContentsDto {

    @Schema(description = "주문 번호", defaultValue = "ex) 12134213-223")
    private Long id;

    @Schema(description = "환불 상태", defaultValue = "ex) 반품, 취소")
    private String orderStatus;
    @Schema(description = "주문 취소 날짜", defaultValue = "ex) 2024.07.24")
    private LocalDateTime cancelTime;
    @Schema(description = "총 주문 취소 상품", defaultValue = "ex) 상품1, 상품2, 상품3")
    private List<BasketPillItemsDto> orderItems;

    public OrderCancelContentsDto(Order order) {
        this.id = order.getId();
        this.orderStatus = order.getPayment().getContent();
        this.cancelTime = order.getUpdateDate();
        this.orderItems = order.getBasket().getBasketPillItems().stream().map((items) -> new BasketPillItemsDto(items)).collect(Collectors.toList());

    }
}
