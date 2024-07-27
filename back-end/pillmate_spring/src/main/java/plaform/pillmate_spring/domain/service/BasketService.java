package plaform.pillmate_spring.domain.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import plaform.pillmate_spring.domain.entity.Basket;
import plaform.pillmate_spring.domain.entity.BasketPillItem;
import plaform.pillmate_spring.domain.entity.Pill;
import plaform.pillmate_spring.domain.repository.BasketPillItemRepository;
import plaform.pillmate_spring.domain.repository.BasketRepository;
import plaform.pillmate_spring.domain.repository.PillRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BasketService {
    private final BasketRepository basketRepository;
    private final BasketPillItemRepository basketPillItemRepository;
    private final PillRepository pillRepository;

    // 아이템 저장
    @Transactional
    public Long addItem(Long basketId, String pillName, int quantity) throws BadRequestException {
        Pill pill = findPill(pillName);
        Basket basket = findBasket(basketId);
        // 중복 상품 존재시 덮여쓰기
        boolean exists = basket.getBasketPillItems().stream()
                .anyMatch(basketPillItem -> basketPillItem.getPill().getName().equals(pillName));

        if (exists) {
            // 해당 이름이 존재할 때의 로직
            List<Long> itemIds = basket.getBasketPillItems().stream()
                    .filter(basketPillItem -> basketPillItem.getPill().getName().equals(pillName))
                    .peek(basketPillItem -> {
                        try {
                            // 새롭게 주문한 상품 개수 반영
                            basketPillItem.addSameItemQuantity(quantity);
                        } catch (BadRequestException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .map(BasketPillItem::getId)  // ID 추출
                    .collect(Collectors.toList());  // 리스트로 수집
            return itemIds.get(0);

        } else {
            BasketPillItem basketPill = BasketPillItem.createBasketPill(pill, quantity);
            BasketPillItem saveBasketPillItem = basketPillItemRepository.save(basketPill);
            basket.addPillItem(saveBasketPillItem);
            return saveBasketPillItem.getId();
        }

    }

    // 아이템 삭제
    @Transactional
    public void removeItem(Long basketId, Long basketPillItemId) throws BadRequestException {
        Basket basket = findBasket(basketId);
        BasketPillItem basketPillItem = findBasketPillItem(basketPillItemId);
        basket.deleteItem(basketPillItem);
    }

    // 아이템 장바구니 수량 변경
    @Transactional
    public void editItemQuantity(Long basketPillItemId, int quantity) throws BadRequestException {
        BasketPillItem basketPillItem = findBasketPillItem(basketPillItemId);
        basketPillItem.addSameItemQuantity(quantity);
    }


    // 아이템 전체 삭제
    @Transactional
    public void allRemoveItem(Long basketId) {
        Basket basket = findBasket(basketId);
        basket.allDeleteItem();
    }

    // 합계
    public int sumItems(Long basketId) {
        Basket basket = findBasket(basketId);
        return basket.itemTotalPrice();
    }

    // 현재 장바구니 아이템 조회
    public List<BasketPillItem> pillItemCheck(Long basketId) {
        Basket basket = findBasket(basketId);
        return basket.getBasketPillItems();
    }

    public List<Basket> basketAllCheck() {
        return basketRepository.findAll();
    }

    private Pill findPill(String pillName) throws BadRequestException {
        Optional<Pill> findOptionalPill = pillRepository.findByName(pillName);
        Pill pill = ValidationService.validationPill(findOptionalPill);
        return pill;
    }

    private Basket findBasket(Long basketId) {
        Optional<Basket> findOptionalBasket = basketRepository.findById(basketId);
        Basket basket = ValidationService.validationBasket(findOptionalBasket);
        return basket;
    }

    private BasketPillItem findBasketPillItem(Long basketPillItemId) throws BadRequestException {
        Optional<BasketPillItem> findOptionalBpi = basketPillItemRepository.findById(basketPillItemId);
        BasketPillItem basketPillItem = ValidationService.validationBasketPillItem(findOptionalBpi);
        return basketPillItem;
    }


}
