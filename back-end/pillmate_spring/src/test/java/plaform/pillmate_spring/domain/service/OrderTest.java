package plaform.pillmate_spring.domain.service;


import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import plaform.pillmate_spring.domain.dto.MemberLoginOAuth;
import plaform.pillmate_spring.domain.dto.PillRequestData;
import plaform.pillmate_spring.domain.entity.Basket;
import plaform.pillmate_spring.domain.entity.Member;
import plaform.pillmate_spring.domain.entity.Order;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class OrderTest {

    @Autowired
    OrderService orderService;
    @Autowired
    BasketService basketService;
    @Autowired
    MemberService memberService;
    @Autowired
    PillService pillService;

    Long memberId;
    List<String> pillNames = new ArrayList<>();

    @BeforeEach
    void 시작후_초기화() throws BadRequestException {
        pillService.AllPillRemove();
        memberService.removeAllUser();
        MemberLoginOAuth memberOne = MemberLoginOAuth.builder()
                .username("m1")
                .email("hello@h.com")
                .nickname("vvs")
                .name("memberOne")
                .role("ROLE_USER")
                .gender("M")
                .password("1234").build();
        Long joinId = memberService.join((memberOne));
        memberId = joinId;
        PillRequestData pillRequestData1 = PillRequestData.builder()
                .name("미래트리메부틴정 100mg/병")
                .classNo("[02390]기타의 소화기관용약")
                .imgKey("http://connectdi.com/design/img/drug/147427567771100198.jpg")
                .dlMaterial("트리메부틴말레산염")
                .dlMaterialEn("Trimebutine")
                .dlCustomShape("정제")
                .colorClass1("연두")
                .drugShape("원형")
                .build();
        PillRequestData pillRequestData2 = PillRequestData.builder()
                .name("큐레틴정(빌베리건조엑스)")
                .classNo("[01310]안과용제")
                .imgKey("http://connectdi.com/design/img/drug/147428234068200039.jpg")
                .dlMaterial("빌베리건조엑스")
                .dlMaterialEn("Bilberry Dried Ext.")
                .dlCustomShape("정제")
                .colorClass1("갈색")
                .drugShape("타원형")
                .build();
        PillRequestData pillRequestData3 = PillRequestData.builder()
                .name("원더칼-디츄어블정")
                .classNo("[03210]칼슘제")
                .imgKey("https://www.pharm.or.kr/images/sb_photo/big3/201001150001701.jpg")
                .dlMaterial("콜레칼시페롤농축분말|구연산칼슘")
                .dlMaterialEn("Calcium Citrate| Cholecalciferol Concentrated Powder")
                .dlCustomShape("정제")
                .colorClass1("하양")
                .drugShape("원형")
                .build();
        PillRequestData pillRequestData4 = PillRequestData.builder()
                .name("뮤코펙트정(암브록솔염산염)")
                .classNo("[02220]진해거담제")
                .imgKey("https://www.pharm.or.kr/images/sb_photo/big3/200907150540503.jpg")
                .dlMaterial("암브록솔염산염")
                .dlMaterialEn("Ambroxol Hydrochloride")
                .dlCustomShape("정제")
                .colorClass1("하양")
                .drugShape("원형")
                .build();
        pillNames.add(pillRequestData1.getName());
        pillNames.add(pillRequestData2.getName());
        pillNames.add(pillRequestData3.getName());
        pillNames.add(pillRequestData4.getName());
        pillService.join(pillRequestData1);
        pillService.join(pillRequestData2);
        pillService.join(pillRequestData3);
        pillService.join(pillRequestData4);
    }

    @DisplayName("장바구니에 있는 상품 주문하기 -> Long order(Long memberId, String SuccessContent, String tid)")
    @Test
    void 장바구니_상품_주문() throws Exception {
        //given
        Member member = memberService.find(memberId);
        Basket basket = member.getBasket();
        Long basketId = basket.getId();
        //when
        basketService.addItem(basketId, pillNames.get(0), 5);
        basketService.addItem(basketId, pillNames.get(1), 3);
        basketService.addItem(basketId, pillNames.get(1), 5);
        Long orderId = orderService.order(member.getId(), "총 2개의 상품들을 주문하였습니다.", "tid1");
        Order order = orderService.orderSingleCheck(orderId);
        List<Order> orders = orderService.orderCheck(member.getId());
        List<Basket> baskets = basketService.basketAllCheck();
        //then
        assertThat(orders.size()).isEqualTo(1);
        assertThat(order).extracting("member").isEqualTo(member);
        assertThat(order.getPayment()).extracting("order").isEqualTo(order);
        assertThat(baskets.size()).isEqualTo(2);
    }
    @DisplayName("주문 취소하기-> orderCancel(Long orderId, String refundContent, String tid)")
    @Test
    void 주문_취소() throws Exception {
        //given
        Member member = memberService.find(memberId);
        Basket basket = member.getBasket();
        Long basketId = basket.getId();
        basketService.addItem(basketId, pillNames.get(0), 5);
        basketService.addItem(basketId, pillNames.get(1), 3);
        basketService.addItem(basketId, pillNames.get(1), 5);
        Long orderId = orderService.order(member.getId(), "총 2개의 상품들을 주문하였습니다.", "tid1");
        //when
        orderService.orderCancel(orderId, "주문 취소", "tid2");
        List<Order> orders = orderService.orderCheck(member.getId());
        List<Order> orderRefunds = orderService.orderRefundCheck(member.getId());
        //then
        assertThat(orders.size()).isEqualTo(0);
        assertThat(orderRefunds.size()).isEqualTo(1);
        assertThat(orderRefunds.get(0)).extracting("member").isEqualTo(member);
    }
    @DisplayName("주문내역 삭제하기 -> removeOrder(Long orderId)")
    @Test
    void 주문내역_삭제() throws Exception {
        //given
        Member member = memberService.find(memberId);
        Basket basket = member.getBasket();
        Long basketId = basket.getId();
        basketService.addItem(basketId, pillNames.get(0), 5);
        basketService.addItem(basketId, pillNames.get(1), 3);
        basketService.addItem(basketId, pillNames.get(1), 5);
        Long orderId = orderService.order(member.getId(), "총 2개의 상품들을 주문하였습니다.", "tid1");
        //when
        orderService.removeOrder(orderId);
        Order order = orderService.orderSingleCheck(orderId);
        //then
        assertThat(order.getPayment().getContent()).isEqualTo("해당 내역은 삭제된 내역입니다.");

    }
}