package plaform.pillmate_spring.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tid;
    private String username;
    private String itemName;
    private int amount;
    private String deliveryStatusKr;


    public static OrderInfo createOrderInfo(String tid, String username, String itemName, int amount, DeliveryStatus deliveryStatus) {
        OrderInfo orderInfo = OrderInfo.builder()
                .tid(tid)
                .username(username)
                .itemName(itemName)
                .amount(amount)
                .deliveryStatusKr(deliveryStatus.getDescription()).build();
        return orderInfo;
    }
}
