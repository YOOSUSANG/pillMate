package plaform.pillmate_spring.domain.service;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import plaform.pillmate_spring.domain.entity.*;

import java.util.Optional;
import java.util.prefs.BackingStoreException;

@Service
public class ValidationService {

    public static void validationLoginMember(Optional<Member> findOptionalMember) throws BadRequestException {
        if (findOptionalMember.isPresent()) {
            throw new BadRequestException("이미 회원 존재합니다.");
        }

    }

    public static Member validationMember(Optional<Member> findOptionalMember) throws BadRequestException {
        if (findOptionalMember.isPresent()) {
            return findOptionalMember.get();
        } else
            throw new BadRequestException("회원을 찾을 수 없습니다");
    }

    public static Pill validationPill(Optional<Pill> findOptionalPill) throws BadRequestException {
        if (findOptionalPill.isPresent()) {
            return findOptionalPill.get();
        } else
            throw new BadRequestException("해당 알약을 찾을 수 없습니다");
    }

    public static Basket validationBasket(Optional<Basket> findOptionalBasket) {
        if (findOptionalBasket.isPresent()) {
            return findOptionalBasket.get();
        } else
            throw new RuntimeException("현재 장바구니가 없습니다.");
    }

    public static BasketPillItem validationBasketPillItem(Optional<BasketPillItem> findOptionalBasketPillItem) throws BadRequestException {
        if (findOptionalBasketPillItem.isPresent()) {
            return findOptionalBasketPillItem.get();
        } else
            throw new BadRequestException("현재 주문한 상품이 존재하지 않습니다.");
    }

    public static Order validationOrder(Optional<Order> findOptionalOrder) throws BadRequestException {
        if (findOptionalOrder.isPresent()) {
            return findOptionalOrder.get();
        } else
            throw new BadRequestException("현재 주문한 상품이 존재하지 않습니다.");
    }


}
