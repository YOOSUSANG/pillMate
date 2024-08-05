package plaform.pillmate_spring.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Order extends BasicEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @OneToOne(fetch = LAZY, cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "basket_id")
    private Basket basket;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    // ***** 연관 메서드 *****
    public void changeBasket(Basket basket) {
        this.basket = basket;
        basket.changeOrder(this);
    }

    public void changeMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void changeDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.changeOrder(this);
    }

    public void changePayment(Payment payment) {
        this.payment = payment;
        payment.changeOrder(this);
    }

    // ***** 비지니스 메서드 *****
    public void orderSuccess() {
        this.delivery.changeDeliveryStatus(DeliveryStatus.READY);
    }

    public void orderRefund() {
        this.delivery.changeDeliveryStatus(DeliveryStatus.CANCEL);
        // 주문한 알약 개수 원복 해당 아이템을 삭제를 안하는 이유는 어떤 상품을 환불했는지 고객에게 알려주기 위해서
        basket.getBasketPillItems().forEach((item) -> item.addPillQuantity());
    }

    // ***** 생성 메서드 *****
    public static Order createPillOrder(Basket basket) {
        Order order = new Order();
        Delivery delivery = new Delivery();
        order.changeDelivery(delivery);
        order.changeBasket(basket);
        order.changeMember(basket.getMember());
        order.orderSuccess();
        return order;
    }
}
