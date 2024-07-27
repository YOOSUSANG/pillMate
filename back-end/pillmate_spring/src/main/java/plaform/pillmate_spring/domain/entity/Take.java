package plaform.pillmate_spring.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Take extends BasicEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pill_take_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "pill_id")
    private Pill pill;


    // ***** 연관 메서드 *****
    public void changeMember(Member member) {
        this.member = member;
        member.getTakes().add(this);
    }

    public void changePill(Pill pill) {
        this.pill = pill;
        pill.changeTake(this);
    }


    //***** 생성 메서드 *****
    public static Take createTake(Member member, Pill pill) {
        Take take = new Take();
        take.changeMember(member);
        take.changePill(pill);
        return take;
    }

}
