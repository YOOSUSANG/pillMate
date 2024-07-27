package plaform.pillmate_spring.domain.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DiscriminatorValue("success")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SuccessPayment extends Payment{

    public SuccessPayment(int totalPrice, String tid, String content) {
        super(totalPrice, tid, content);
    }

    // ***** 셍성 메서드 *****
    public static SuccessPayment createSuccessPayment(Order order, String buyContent, int totalPrice, String tid){
        SuccessPayment successPayment = new SuccessPayment(totalPrice, tid, buyContent);
        order.changePayment(successPayment);
        return successPayment;
    }

}
