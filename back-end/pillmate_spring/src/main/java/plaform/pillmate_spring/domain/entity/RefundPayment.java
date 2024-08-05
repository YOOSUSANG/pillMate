package plaform.pillmate_spring.domain.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DiscriminatorValue("refund")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefundPayment extends Payment {

    public RefundPayment(int totalPrice, String tid, String content) {
        super(totalPrice, tid, content);
    }

    // ***** 셍성 메서드 *****
    public static RefundPayment createRefundPayment(String buyContent, int totalPrice, String tid) {
        RefundPayment refundPayment = new RefundPayment(totalPrice, tid, buyContent);
        return refundPayment;
    }

    public static void stateChangeToRefund(Payment payment, Order order) {
        // 환불 내역으로 변경
        order.changePayment(payment);
        // 주문 상태를 환불로 변경 및 환불시 해당 주문한 아이템 개수 원복
        order.orderRefund();
    }

}
