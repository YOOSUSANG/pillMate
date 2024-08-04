package plaform.pillmate_spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import plaform.pillmate_spring.domain.entity.Pill;

@Data
@Schema(description = "상품 목록 DTO")
@AllArgsConstructor
@NoArgsConstructor
public class PillRegisterInfoDto {

    @Schema(description = "상품 고유 식별자", defaultValue = "ex) 1, 2, 3, 4")
    private Long id;
    @Schema(description = "상품 이름", defaultValue = "ex) 타이레놀1, 타이레놀2, 타이레놀3")
    private String dl_name;
    @Schema(description = "상품 분류", defaultValue = "ex) 치과구강용약, 기타의 호흡기관용약 ")
    private String dl_class_no;
    @Schema(description = "상품 이미지 URL")
    private String img_key;
    @Schema(description = "상품 성분", defaultValue = "플루르비프로펜, 아스피린장용과립 ")
    private String dl_material;
    @Schema(description = "상품 성분 EN", defaultValue = "Flurbiprofen, Aspirin Enteric Gr ")
    private String dl_material_en;
    @Schema(description = "상품 제형", defaultValue = "정제, 경질캡슐제, 연질캡슐제")
    private String dl_custom_shape;
    @Schema(description = "상품 가격", defaultValue = "ex) 1000, 2000, 3000")
    private int price;
    @Schema(description = "상품 수량", defaultValue = "ex) 1, 2, 3")
    private int quantity;

    public PillRegisterInfoDto(Pill pill) {
        this.id = pill.getId();
        this.dl_name = pill.getName();
        this.dl_class_no = pill.getClassNo();
        this.img_key = pill.getImgKey();
        this.dl_material = pill.getDlMaterial();
        this.dl_material_en = pill.getDlMaterialEn();
        this.dl_custom_shape = pill.getDlCustomShape();
        this.price = pill.getPrice();
        this.quantity = pill.getQuantity();
    }
}
