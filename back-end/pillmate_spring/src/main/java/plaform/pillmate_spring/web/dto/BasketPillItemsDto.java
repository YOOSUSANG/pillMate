package plaform.pillmate_spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import plaform.pillmate_spring.domain.entity.BasketPillItem;

@Data
@Schema(description = "장바구니 상품 DTO")
@AllArgsConstructor
@NoArgsConstructor
public class BasketPillItemsDto {

    @Schema(description = "장바구니 상품 고유 식별자", defaultValue = "ex) 1, 2, 3, 4")
    private Long basketItemId;
    @Schema(description = "상품 고유 식별자", defaultValue = "ex) 1, 2, 3, 4")
    private Long id;
    @Schema(description = "상품 이름", defaultValue = "ex) 타이레놀1, 타이레놀2, 타이레놀3")
    private String dl_name;
    @Schema(description = "상품 분류", defaultValue = "ex) 치과구강용약, 기타의 호흡기관용약 ")
    private String dl_class_no;

    @Schema(description = "상품 가격", defaultValue = "ex) 1000, 2000, 3000")
    private int price;
    @Schema(description = "상품 성분", defaultValue = "플루르비프로펜, 아스피린장용과립 ")
    private String dl_material;
    @Schema(description = "상품 성분 EN", defaultValue = "Flurbiprofen, Aspirin Enteric Gr ")
    private String dl_material_en;

    @Schema(description = "상품 수량", defaultValue = "ex) 1, 2, 3")
    private int quantity;


    @Schema(description = "상품 이미지 URL")
    private String img_key;

    public BasketPillItemsDto(BasketPillItem basketPillItem) {
        this.basketItemId = basketPillItem.getId();
        this.id = basketPillItem.getPill().getId();
        this.dl_name = basketPillItem.getPill().getName();
        this.dl_class_no = basketPillItem.getPill().getClassNo();
        this.dl_material = basketPillItem.getPill().getDlMaterial();
        this.dl_material_en = basketPillItem.getPill().getDlMaterialEn();
        this.price = basketPillItem.getPrice();
        this.quantity = basketPillItem.getSelectQuantity();
        this.img_key = basketPillItem.getPill().getImgKey();
    }
}
