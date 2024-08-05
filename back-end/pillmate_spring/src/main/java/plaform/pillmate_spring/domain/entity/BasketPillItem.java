package plaform.pillmate_spring.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.apache.coyote.BadRequestException;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class BasketPillItem extends BasicEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basket_pill_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "pill_id")
    private Pill pill;

    // 장바구니에 해당 아이템을 담은 수량
    private int selectQuantity;
    // 장바구니에 해당 아이템의 총 가격
    private int price;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "basket_id")
    private Basket basket;

    // ***** 연관 메서드 *****
    public void changePill(Pill pill) {

        this.pill = pill;
        pill.getBasketPillItems().add(this);
    }

    public void changeBasket(Basket basket) {
        this.basket = basket;
    }

    public void changeSelectQuantity(int selectQuantity) {
        this.selectQuantity = selectQuantity;
    }

    // ***** 비지니스 메서드 *****
    public void reducePillQuantity(int quantity) throws BadRequestException {
        this.pill.reduceQuantity(quantity);
    }

    public void addPillQuantity() {
        this.pill.addQuantity(selectQuantity);

    }

    public void calculatePrice() {
        this.price = getPill().getPrice() * selectQuantity;
    }

    public void addSameItemQuantity(int selectQuantity) throws BadRequestException {
        // 기존에 존재하는 상품 개수 반환
        addPillQuantity();
        // 새로운 상품으로 개수 감소
        reducePillQuantity(selectQuantity);
        // 담은 개수 갱신
        changeSelectQuantity(selectQuantity);
        // 담은 개수로 합계 게산
        calculatePrice();
    }

    // ***** 생성 메서드 *****
    public static BasketPillItem createBasketPill(Pill pill, int selectQuantity) throws BadRequestException {
        BasketPillItem basketPillItem = new BasketPillItem();
        basketPillItem.changePill(pill);
        basketPillItem.changeSelectQuantity(selectQuantity);
        basketPillItem.reducePillQuantity(selectQuantity);
        basketPillItem.calculatePrice();
        return basketPillItem;
    }
}
