package plaform.pillmate_spring.domain.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Basket extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basket_id")
    private Long id;

    @OneToOne(fetch = LAZY, mappedBy = "basket")
    private Member member;

    @OneToOne(fetch = LAZY, mappedBy = "basket")
    private Order order;

    @OneToMany(fetch = LAZY, mappedBy = "basket",cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<BasketPillItem> basketPillItems = new ArrayList<>();


    // ***** 연관 메서드 *****
    public void changeOrder(Order order) {
        this.order = order;

    }

    public void changeMember(Member member) {
        this.member = member;
    }

    public void addPillItem(BasketPillItem basketPillItem) {
        getBasketPillItems().add(basketPillItem);
        basketPillItem.changeBasket(this);
    }

    // ***** 비지니스 메서드 *****

    // 장바구니에서 아이템 삭제
    public void deleteItem(BasketPillItem... basketPillItems) {
        System.out.println(basketPillItems.getClass());
        for (BasketPillItem basketPillItem : basketPillItems) {
            basketPillItem.addPillQuantity();
            this.basketPillItems.remove(basketPillItem);
        }
    }

    public void allDeleteItem() {
        // 상품개수 원복 후 삭제
        this.basketPillItems.forEach((items) -> items.addPillQuantity());
        this.basketPillItems.clear();
    }

    public int itemTotalPrice() {
        int totalPrice = 0;
        for (BasketPillItem basketPillItem : basketPillItems) {
            totalPrice += basketPillItem.getPrice();
        }
        return totalPrice;
    }

    // ***** 생성 메서드 *****
    public static Basket createBasket(Member member) {
        Basket basket = new Basket();
        member.changeBasket(basket);
        return basket;
    }


}
