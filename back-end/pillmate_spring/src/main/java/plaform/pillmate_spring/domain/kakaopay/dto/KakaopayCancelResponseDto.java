package plaform.pillmate_spring.domain.kakaopay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KakaopayCancelResponseDto {
    private String aid;
    private String tid;
    private String cid;
    private String status;
    private String partner_order_id;
    private String partner_user_id;
    private String payment_method_type;
    private Amount amount;
    private ApprovedCancelAmount approved_cancel_amount;
    private String item_name;
    private String item_code;
    private LocalDateTime created_at;
    private LocalDateTime approved_at;
    private LocalDateTime canceled_at;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Amount{
        private Integer total;
        private Integer tex_free;
        private Integer discount;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class ApprovedCancelAmount {
        private Integer total;
        private Integer tax_free;
        private Integer discount;
    }
}
