package plaform.pillmate_spring.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Pill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pill_id")
    private Long id;
    private String name;
    private String classNo;
    private String imgKey;
    private String dlMaterial;
    private String dlMaterialEn;
    private String dlCustomShape;
    private String colorClass1;
    private String drugShape;

    // 임의로 설정
    private final int price = 1000;
    private final int amount = 100;

    @OneToMany(fetch = LAZY, mappedBy = "pill")
    private final List<PillOrder> pillOrders = new ArrayList<>();

    @OneToOne(fetch = LAZY, mappedBy = "pill")
    private Take take;

    // ***** 연관 메서드*****
    public void changeTake(Take take) {
        this.take = take;
    }

    // ***** 생성 메서드 *****
    public static Pill createPill(String name, String classNo, String imgKey, String dlMaterial, String dlMaterialEn, String dlCustomShape, String colorClass1, String drugShape) {
        Pill pill = Pill.builder()
                .name(name)
                .imgKey(imgKey)
                .dlMaterial(dlMaterial)
                .dlMaterialEn(dlMaterialEn)
                .dlCustomShape(dlCustomShape)
                .colorClass1(colorClass1)
                .drugShape(drugShape)
                .build();
        return pill;
    }

}
