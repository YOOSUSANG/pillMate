package plaform.pillmate_spring.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import plaform.pillmate_spring.domain.entity.*;
import plaform.pillmate_spring.domain.kakaopay.KakaoPayProperties;
import plaform.pillmate_spring.domain.kakaopay.dto.*;
import plaform.pillmate_spring.domain.kakaopay.service.PayService;
import plaform.pillmate_spring.domain.oauth2.CustomOAuth2User;
import plaform.pillmate_spring.domain.repository.OrderInfoRepository;
import plaform.pillmate_spring.domain.service.MemberService;
import plaform.pillmate_spring.domain.service.OrderService;
import plaform.pillmate_spring.domain.service.PaymentService;
import plaform.pillmate_spring.web.dto.JsonResult;
import plaform.pillmate_spring.web.dto.OrderCancelContentsDto;
import plaform.pillmate_spring.web.dto.OrderContentsDto;
import plaform.pillmate_spring.web.dto.OrderSuccessDto;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Tag(name = "주문 API", description = "카카오페이 결제를 통한 주문")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final MemberService memberService;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final KakaoPayProperties kakaoPayProperties;
    private final OrderInfoRepository orderInfoRepository;
    private final CommonPayInfo commonPayInfo = new CommonPayInfo();


    @Operation(summary = "결제 준비")
    @ApiResponse(responseCode = "200", description = "결제 준비 성공", content = {
            @Content(schema = @Schema(implementation = KakaopayReadyResponseDto.class))
    })
    @PostMapping("/orders/billings")
    public ResponseEntity<?> basketPaymentReady(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) throws BadRequestException {
        PayService payService = new PayService(kakaoPayProperties);
        String username = customOAuth2User.getUsername();
        Member member = memberService.findUsername(username);
        Basket basket = member.getBasket();
        // 상품 결제 내용 생성 (대표적으로 맨 처음 장바구니에 담은 알약이름으로 지정)
        BasketPillItem generalBasketItem = basket.getBasketPillItems().get(0);
        int itemsTotalQuantity = basket.getBasketPillItems().size();
        String itemName;
        if (itemsTotalQuantity == 1) {
            itemName = generalBasketItem.getPill().getName();
        } else {
            itemName = generalBasketItem.getPill().getName() + " 외 " + (itemsTotalQuantity - 1) + "건";
        }
        int totalCount = paymentService.totalCount();
        // request
        KakaopayReadyRequestDto dto = KakaopayReadyRequestDto.builder()
                .cid(kakaoPayProperties.getCid())
                .partnerOrderId(Integer.toString(totalCount + 1))
                .partnerUserId(kakaoPayProperties.getCname())
                .itemName(itemName)
                .quantity(itemsTotalQuantity)
                .totalAmount(basket.itemTotalPrice())
                .taxFreeAmount(0)
                .approvalUrl("http://localhost:8080/payment/success")
                .cancelUrl("http://localhost:8080/payment/fail")
                .failUrl("http://localhost:8080/payment/cancel")
                .build();
        KakaopayReadyResponseDto kakaopayReadyResponseDto = payService.kakaoPayReady(dto);
        commonPayInfo.setTid(kakaopayReadyResponseDto.getTid());
        // 리액트에서 next_redirect_pc_url에 대해서 리다이렉트할 필요가 있음
        log.info("{} 카카오페이 결제 준비 response 반환", kakaopayReadyResponseDto);
        return ResponseEntity.ok().body(kakaopayReadyResponseDto);
    }

    @Operation(summary = "결제 승인")
    @ApiResponse(responseCode = "302", description = "결제 승인 성공", content = {
            @Content(schema = @Schema(implementation = KakaopayApproveResponseDto.class))
    })
    @GetMapping("/payment/success")
    public ResponseEntity<?> basketPaymentApprove(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @RequestParam("pg_token") String pgToken) throws IOException {
        log.info("pgToken : {}", pgToken);
        PayService payService = new PayService(kakaoPayProperties);
        String username = customOAuth2User.getUsername();
        Member member = memberService.findUsername(username);
        Basket basket = member.getBasket();
        int totalCount = paymentService.totalCount();
        KakaopayApproveRequestDto dto = KakaopayApproveRequestDto.builder()
                .tid(commonPayInfo.getTid())
                .cid(kakaoPayProperties.getCid())
                .partnerOrderId(Integer.toString(totalCount + 1))
                .partnerUserId(kakaoPayProperties.getCname())
                .pgToken(pgToken)
                .build();
        KakaopayApproveResponseDto kakaopayApproveResponseDto = payService.kakaoPayApprove(dto);
        // 결제 내역 저장 후 주문 완료라는 페이지로 넘기기(수정필요)-> 이떄 302로 redirect
        String redirectUrl = "http://localhost:5173/order/success";
        HttpHeaders httpHeaders = new HttpHeaders();
        // location에 redirect 정보 넣기
        httpHeaders.setLocation(URI.create(redirectUrl));
        // 주문 로직 완료
        Long orderId = orderService.order(member.getId(), "주문", commonPayInfo.getTid());
        Order order = orderService.orderSingleCheck(orderId);
        DeliveryStatus deliveryStatus = order.getDelivery().getDeliveryStatus();
        OrderInfo orderInfo = OrderInfo.createOrderInfo(kakaopayApproveResponseDto.getTid(), username, kakaopayApproveResponseDto.getItem_name(), kakaopayApproveResponseDto.returnTotal(), deliveryStatus);
        orderInfoRepository.save(orderInfo);
        // 302:  POST -> GET
        return ResponseEntity.status(HttpStatus.FOUND).headers(httpHeaders).build();
    }

    @Operation(summary = "결제 환불")
    @ApiResponse(responseCode = "200", description = "결제 환불 성공", content = {
            @Content(schema = @Schema(implementation = KakaopayCancelResponseDto.class))
    })
    @PostMapping("orders/{orderId}/RefundBillings")
    public ResponseEntity<?> basketPaymentCancel(@PathVariable("orderId") Long orderId) throws BadRequestException {
        PayService payService = new PayService(kakaoPayProperties);
        Order order = orderService.orderSingleCheck(orderId);
        String tid = order.getPayment().getTid();
        int totalPrice = order.getPayment().getTotalPrice();
        KakaopayCancelRequestDto dto = KakaopayCancelRequestDto.builder()
                .cid(kakaoPayProperties.getCid())
                .tid(tid)
                .cancelAmount(totalPrice)
                .cancelTaxFreeAmount(0)
                .build();
        KakaopayCancelResponseDto kakaopayCancelResponseDto = payService.kakaoPayCancel(dto);
        // 환불 요청
        orderService.orderCancel(orderId, "반품", tid);
        return ResponseEntity.ok().body(kakaopayCancelResponseDto);
    }

    @Operation(summary = "회원 주문완료목록 조회")
    @ApiResponse(responseCode = "200", description = "주문완료목록 조회 성공", content = {
            @Content(schema = @Schema(implementation = OrderContentsDto.class))
    })
    @GetMapping("/members/orders")
    public ResponseEntity<?> getMemberCompleteOrders(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) throws BadRequestException {
        String username = customOAuth2User.getUsername();
        Member member = memberService.findUsername(username);
        List<Order> orders = orderService.orderCheck(member.getId());
        List<OrderContentsDto> ordersDto = orders.stream().map((order) -> new OrderContentsDto(order)).collect(Collectors.toList());
        return ResponseEntity.ok().body(new JsonResult<>(ordersDto));
    }

    @Operation(summary = "회원 주문취소목록 조회")
    @ApiResponse(responseCode = "200", description = "주문취소목록 조회 성공", content = {
            @Content(schema = @Schema(implementation = OrderCancelContentsDto.class))
    })
    @GetMapping("/members/cancels")
    public ResponseEntity<?> getMemberCancelOrders(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) throws BadRequestException {
        String username = customOAuth2User.getUsername();
        Member member = memberService.findUsername(username);
        List<Order> orders = orderService.orderRefundCheck(member.getId());
        List<OrderCancelContentsDto> ordersDto = orders.stream().map((order) -> new OrderCancelContentsDto(order)).collect(Collectors.toList());
        return ResponseEntity.ok().body(new JsonResult<>(ordersDto));
    }

    @Operation(summary = "주문 내역 삭제")
    @ApiResponse(responseCode = "200", description = "주문 내역 삭제 성공")
    @PostMapping("/orders/{orderId}/delete")
    public ResponseEntity<?> deleteOrder(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @PathVariable(name = "orderId") Long orderId) throws BadRequestException {
        orderService.removeOrder(orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "주문완료 조회")
    @ApiResponse(responseCode = "200", description = "주문완료 조회 성공", content = {
            @Content(schema = @Schema(implementation = OrderSuccessDto.class))
    })
    @PostMapping("/order_info")
    public ResponseEntity<?> getOrderInfo(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) throws BadRequestException {
        String username = customOAuth2User.getUsername();
        OrderInfo findOrderInfo = orderInfoRepository.findByUsername(username);
        OrderSuccessDto orderSuccessDto = new OrderSuccessDto(findOrderInfo);
        orderInfoRepository.delete(findOrderInfo);
        return ResponseEntity.ok().body(orderSuccessDto);
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class CommonPayInfo {
        private String tid;
        private Long itemId;
    }


}
