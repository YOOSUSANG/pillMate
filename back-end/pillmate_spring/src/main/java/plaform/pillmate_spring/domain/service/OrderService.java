package plaform.pillmate_spring.domain.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import plaform.pillmate_spring.domain.entity.*;
import plaform.pillmate_spring.domain.repository.BasketRepository;
import plaform.pillmate_spring.domain.repository.MemberRepository;
import plaform.pillmate_spring.domain.repository.OrderRepository;
import plaform.pillmate_spring.domain.repository.PaymentRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final BasketRepository basketRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;


    // 주문
    @Transactional
    public Long order(Long memberId, String SuccessContent, String tid) throws BadRequestException {
        Member member = findMember(memberId);
        Basket basket = member.getBasket();
        Order order = Order.createPillOrder(basket);
        Order successOrder = orderRepository.save(order);
        SuccessPayment savePayment = SuccessPayment.createSuccessPayment(successOrder, SuccessContent, basket.itemTotalPrice(), tid);
        paymentRepository.save(savePayment);

        member.changeBasket(null);
        Basket newBasket = Basket.createBasket(member);
        basketRepository.save(newBasket);

        return successOrder.getId();
    }


    // 환불
    @Transactional
    public Long orderCancel(Long orderId, String cancelContent, String tid) throws BadRequestException {
        Order order = findOrder(orderId);
        RefundPayment refundPayment = RefundPayment.createRefundPayment(cancelContent, order.getBasket().itemTotalPrice(), tid);
        RefundPayment saveRefundPayment = paymentRepository.save(refundPayment);
        paymentRepository.deleteById(order.getPayment().getId());
        RefundPayment.stateChangeToRefund(saveRefundPayment, order);
        return order.getId();
    }

    // 주문내역 삭제
    @Transactional
    public void removeOrder(Long orderId) throws BadRequestException {
        Order order = findOrder(orderId);
        String tid = order.getPayment().getTid();
        DeletePayment deletePayment = DeletePayment.createDeletePayment("해당 내역은 삭제된 내역입니다.", order.getBasket().itemTotalPrice(), tid);
        DeletePayment saveDeletePayment = paymentRepository.save(deletePayment);
        paymentRepository.deleteById(order.getPayment().getId());
        DeletePayment.stateChangeToDelete(saveDeletePayment, order);
    }

    // 주문조회
    public List<Order> orderCheck(Long memberId) {
        return orderRepository.findAllByMemberIdAndPaymentDtype(memberId, "success");
    }

    public Order orderSingleCheck(Long orderId) throws BadRequestException {
        Order order = findOrder(orderId);
        return order;
    }

    // 환불 조회
    public List<Order> orderRefundCheck(Long memberId) {
        return orderRepository.findAllByMemberIdAndPaymentDtype(memberId, "refund");
    }

    private Basket findBasket(Long basketId) {
        Optional<Basket> findOptionalBasket = basketRepository.findById(basketId);
        Basket basket = ValidationService.validationBasket(findOptionalBasket);
        return basket;
    }

    private Member findMember(Long memberId) throws BadRequestException {
        Optional<Member> findOptionalMember = memberRepository.findById(memberId);
        Member member = ValidationService.validationMember(findOptionalMember);
        return member;
    }

    private Order findOrder(Long orderId) throws BadRequestException {
        Optional<Order> findOptionalOrder = orderRepository.findById(orderId);
        Order order = ValidationService.validationOrder(findOptionalOrder);
        return order;
    }
}
