package plaform.pillmate_spring.domain.entity;


import lombok.Getter;

@Getter
public enum DeliveryStatus {
    READY("배송준비"),
    CANCEL("주문취소"),
    COMP("배송도착");

    private final String description;

    DeliveryStatus(String description) {
        this.description = description;
    }

}
