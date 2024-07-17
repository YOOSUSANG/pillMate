package plaform.pillmate_spring.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PillOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pill_storage_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "pill_id")
    private Pill pill;


    // ***** 연관 메서드 *****
    public void changePill(Pill pill) {
        this.pill = pill;
        pill.getPillOrders().add(this);
    }



    // ***** 생성 메서드 *****
    public static PillOrder createPillOrder(Pill pill) {
        PillOrder pillOrder = new PillOrder();
        pillOrder.changePill(pill);
        return pillOrder;
    }
}
