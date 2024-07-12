package plaform.pillmate_spring;

import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import plaform.pillmate_spring.domain.dto.MemberLoginOAuth;
import plaform.pillmate_spring.domain.entity.History;
import plaform.pillmate_spring.domain.entity.Member;
import plaform.pillmate_spring.domain.service.HistoryService;
import plaform.pillmate_spring.domain.service.MemberService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class HistoryTest {
    @Autowired
    HistoryService historyService;
    @Autowired
    MemberService memberService;
    Long memberId;
    @BeforeEach
    void 시작후_초기화() throws BadRequestException {
        historyService.AllPillRemove();
        memberService.removeAllUser();
        MemberLoginOAuth memberOne = MemberLoginOAuth.builder()
                .username("m1")
                .email("hello@h.com")
                .name("memberOne")
                .password("1234").build();
        Long joinId = memberService.join((memberOne));
        memberId = joinId;
    }

    @DisplayName("유저 복용 이력 추가")
    @Test
    void 복용이력_추가() throws BadRequestException {
        History hisOne = new History(memberId, "약이름1");
        History hisTwo = new History(memberId, "약이름2");
        historyService.join(hisOne);
        historyService.join(hisTwo);
        List<History> takePill = historyService.findTakePill(memberId);
        assertThat(takePill.size()).isEqualTo(2);
    }

    @DisplayName("유저 복용 이력 삭제")
    @Test
    void 복용이력_삭제(){
        History hisOne = new History(memberId, "약이름1");
        History hisTwo = new History(memberId, "약이름2");
        historyService.join(hisOne);
        historyService.join(hisTwo);
        String pillName = "약이름1";
        historyService.removeTakePill(memberId,pillName);
        List<History> takePill = historyService.findTakePill(memberId);
        assertThat(takePill.size()).isEqualTo(1);
    }

    @DisplayName("유저 복용 이력 삭제 실패")
    @Test
    void 복용이력_삭제실패(){
        History hisOne = new History(memberId, "약이름1");
        History hisTwo = new History(memberId, "약이름2");
        historyService.join(hisOne);
        historyService.join(hisTwo);
        String pillName = "약이름3";
        historyService.removeTakePill(memberId,pillName);
        List<History> takePill = historyService.findTakePill(memberId);
        assertThat(takePill.size()).isEqualTo(2);
    }
}
