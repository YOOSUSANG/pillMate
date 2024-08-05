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
import plaform.pillmate_spring.domain.entity.BasketPillItem;
import plaform.pillmate_spring.domain.entity.Member;
import plaform.pillmate_spring.domain.entity.Pill;
import plaform.pillmate_spring.domain.repository.BasketPillItemRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class BasketTest {

    @Autowired
    BasketService basketService;
    @Autowired
    MemberService memberService;
    @Autowired
    PillService pillService;
    @Autowired
    BasketPillItemRepository basketPillItemRepository;

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

    @DisplayName("장바구니에 아이템 추가하기 -> addItem(Long basketId, String pillName, int quantity)")
    @Test
    void 장바구니_아이템_추가() throws Exception {
        //given
        Member member = memberService.find(memberId);
        Basket basket = member.getBasket();
        Long basketId = basket.getId();
        //when
        basketService.addItem(basketId, pillNames.get(0), 5);
        basketService.addItem(basketId, pillNames.get(1), 3);
        basketService.addItem(basketId, pillNames.get(1), 5);
        List<BasketPillItem> basketPillItems = basketService.pillItemCheck(basketId);
        int totalPrice = basketService.sumItems(basketId);
        //then
        assertThat(basketPillItems.size()).isEqualTo(2);
        assertThat(totalPrice).isEqualTo(10000);
        assertThat(basketPillItems.get(0).getPill().getQuantity()).isEqualTo(95);
        assertThat(basketPillItems.get(1).getPill().getQuantity()).isEqualTo(95);
    }
    @DisplayName("장바구니 아이템 삭제 -> removeItem()")
    @Test
    void 장바구니_아이템_삭제1() throws Exception {
        Member member = memberService.find(memberId);
        Basket basket = member.getBasket();
        Pill pill = pillService.find(pillNames.get(0));
        Long basketId = basket.getId();
        Long addItem1 = basketService.addItem(basketId, pillNames.get(0), 5);
        Long addItem2 = basketService.addItem(basketId, pillNames.get(1), 3);
        Long addItem3 = basketService.addItem(basketId, pillNames.get(1), 3);
        //when
        basketService.removeItem(basketId, addItem1);
        List<BasketPillItem> basketPillItems = basketService.pillItemCheck(basketId);
        List<BasketPillItem> all = basketPillItemRepository.findAll();
        //then
        assertThat(basketPillItems.size()).isEqualTo(1);
        assertThat(pill.getQuantity()).isEqualTo(100);
        assertThat(all.size()).isEqualTo(1);

    }
    @DisplayName("장바구니 아이템 삭제 -> removeItem()")
    @Test
    void 장바구니_아이템_삭제2() throws Exception {
        Member member = memberService.find(memberId);
        Basket basket = member.getBasket();
        Pill pill = pillService.find(pillNames.get(1));
        Long basketId = basket.getId();
        Long addItem1 = basketService.addItem(basketId, pillNames.get(0), 5);
        Long addItem2 = basketService.addItem(basketId, pillNames.get(1), 3);
        Long addItem3 = basketService.addItem(basketId, pillNames.get(1), 5);
        //when
        basketService.removeItem(basketId, addItem3);
        List<BasketPillItem> basketPillItems = basketService.pillItemCheck(basketId);
        List<BasketPillItem> all = basketPillItemRepository.findAll();
        //then
        assertThat(basketPillItems.size()).isEqualTo(1);
        assertThat(pill.getQuantity()).isEqualTo(100);
        assertThat(all.size()).isEqualTo(1);

    }
    @DisplayName("장바구니 아이템 전제 삭제 -> allRemoveItem()")
    @Test
    void 장바구니_아이템_전체삭제() throws Exception {
        //given
        Member member = memberService.find(memberId);
        Basket basket = member.getBasket();
        Long basketId = basket.getId();
        Long addItem1 = basketService.addItem(basketId, pillNames.get(0), 5);
        Long addItem2 = basketService.addItem(basketId, pillNames.get(1), 3);
        Long addItem3 = basketService.addItem(basketId, pillNames.get(1), 5);
        //when
        basketService.allRemoveItem(basketId);
        Pill pill1 = pillService.find(addItem1);
        Pill pill2 = pillService.find((addItem2));
        Pill pill3 = pillService.find(addItem3);
        List<BasketPillItem> basketPillItems = basket.getBasketPillItems();
        //then
        assertThat(pill1.getQuantity()).isEqualTo(100);
        assertThat(pill2.getQuantity()).isEqualTo(100);
        assertThat(pill3.getQuantity()).isEqualTo(100);
        assertThat(basketPillItems.size()).isEqualTo(0);

    }

}