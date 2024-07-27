package plaform.pillmate_spring.domain.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DiscriminatorValue("delete")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeletePayment extends Payment {

    public DeletePayment(int totalPrice, String tid, String content) {
        super(totalPrice, tid, content);
    }

    // ***** 셍성 메서드 *****
    public static DeletePayment createDeletePayment(String buyContent, int totalPrice, String tid) {
        DeletePayment deletePayment = new DeletePayment(totalPrice, tid, buyContent);
        return deletePayment;
    }

    public static void stateChangeToDelete(Payment payment, Order order) {
        // 삭제 내역으로 변경
        order.changePayment(payment);
    }
}
