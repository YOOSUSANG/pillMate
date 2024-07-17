package plaform.pillmate_spring;

import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import plaform.pillmate_spring.domain.dto.MemberLoginOAuth;
import plaform.pillmate_spring.domain.dto.PillRequestData;
import plaform.pillmate_spring.domain.entity.Member;
import plaform.pillmate_spring.domain.entity.Take;
import plaform.pillmate_spring.domain.service.PillService;
import plaform.pillmate_spring.domain.service.MemberService;
import plaform.pillmate_spring.domain.service.TakeService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PillTest {
    @Autowired
    PillService pillService;
    @Autowired
    MemberService memberService;
    @Autowired
    TakeService takeService;
    Long memberId;
    String pillNameOne = "미래트리메부틴정 100mg/병";
    String pillNameTwo = "큐레틴정(빌베리건조엑스)";

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
        pillService.join(pillRequestData1);
        pillService.join(pillRequestData2);

    }

    @DisplayName("유저 복용 이력 추가")
    @Test
    void 복용이력_추가() throws BadRequestException {
        Member member = memberService.find(memberId);
        takeService.pillTakeOne(member.getId(), pillNameOne);
        takeService.pillTakeOne(member.getId(), pillNameTwo);
        List<Take> takePill = takeService.findTakePill(member.getId());
        assertThat(takePill.size()).isEqualTo(2);
    }

    @DisplayName("유저 복용 이력 삭제")
    @Test
    void 복용이력_삭제() throws BadRequestException {
        Member member = memberService.find(memberId);
        takeService.pillTakeOne(member.getId(), pillNameOne);
        takeService.pillTakeOne(member.getId(), pillNameTwo);
        takeService.removeTakePill(member.getId(), pillNameTwo);
        List<Take> takePill = takeService.findTakePill(member.getId());
        assertThat(takePill.size()).isEqualTo(1);
    }

    @DisplayName("유저 복용 이력 삭제 실패")
    @Test
    void 복용이력_삭제실패() throws BadRequestException {
        Member member = memberService.find(memberId);
        takeService.pillTakeOne(member.getId(), pillNameOne);
        takeService.pillTakeOne(member.getId(), pillNameTwo);
        takeService.removeTakePill(member.getId(),"아무약");
        List<Take> takePill = takeService.findTakePill(member.getId());
        assertThat(takePill.size()).isEqualTo(2);
    }
}
