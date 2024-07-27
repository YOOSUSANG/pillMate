package plaform.pillmate_spring.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Payment extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "member_id")
//    private Member member;

    @OneToOne(fetch = LAZY, mappedBy = "payment")
    private Order order;

    private int totalPrice;
    private String tid;
    private String content;

    public Payment(int totalPrice, String tid, String content) {
        this.totalPrice = totalPrice;
        this.tid = tid;
        this.content = content;
    }

    // 자동으로 dtype과 매핑된다.
    @Column(insertable = false, updatable = false)
    private String dtype;

    // ***** 연관 메서드 *****
    public void changeOrder(Order order) {
        this.order = order;
    }

//    public void changeMember(Member member) {
//        this.member = member;
//        if (member != null)
//            member.getPayments().add(this);
//    }


}
