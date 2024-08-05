package plaform.pillmate_spring.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.apache.coyote.BadRequestException;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Pill extends BasicEntity{
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
    private int price;
    private int quantity;

    @OneToMany(fetch = LAZY, mappedBy = "pill")
    private final List<BasketPillItem> basketPillItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, mappedBy = "pill")
    private Take take;

    // ***** 연관 메서드 *****
    public void changeTake(Take take) {
        this.take = take;
    }

    // ***** 비지니스 메서드 *****
    public void reduceQuantity(int quantity) throws BadRequestException {
        if (this.quantity == 0) {
            throw new BadRequestException("해당 알약은 현재 재고 소진입니다.");
        }
        this.quantity -= quantity;

    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    // ***** 생성 메서드 *****
    public static Pill createPill(String name, String classNo, String imgKey, String dlMaterial, String dlMaterialEn, String dlCustomShape, String colorClass1, String drugShape) {
        Pill pill = Pill.builder()
                .name(name)
                .classNo(classNo)
                .imgKey(imgKey)
                .dlMaterial(dlMaterial)
                .dlMaterialEn(dlMaterialEn)
                .dlCustomShape(dlCustomShape)
                .colorClass1(colorClass1)
                .quantity(100)
                .price(1000)
                .drugShape(drugShape)
                .build();
        return pill;
    }

}
