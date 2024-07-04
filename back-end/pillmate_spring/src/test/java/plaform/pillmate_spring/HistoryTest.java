package plaform.pillmate_spring;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import plaform.pillmate_spring.domain.History;
import plaform.pillmate_spring.domain.User;
import plaform.pillmate_spring.service.HistoryService;
import plaform.pillmate_spring.service.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class HistoryTest {
    @Autowired
    HistoryService historyService;
    @Autowired
    UserService userService;

    @BeforeEach
    void 시작후_초기화() {
        historyService.AllPillRemove();
        userService.removeAllUser();
    }

    @DisplayName("유저 복용 이력 추가")
    @Test
    void 복용이력_추가(){
        User userOne = new User("ajou123", "hello1@naver.com", "12345678");
        userService.join((userOne));
        History hisOne = new History(userOne.getId(), "약이름1");
        History hisTwo = new History(userOne.getId(), "약이름2");
        historyService.join(hisOne);
        historyService.join(hisTwo);
        List<History> takePill = historyService.findTakePill(userOne.getId());
        assertThat(takePill.size()).isEqualTo(2);
    }

    @DisplayName("유저 복용 이력 삭제")
    @Test
    void 복용이력_삭제(){
        User userOne = new User("ajou123", "hello1@naver.com", "12345678");
        userService.join((userOne));
        History hisOne = new History(userOne.getId(), "약이름1");
        History hisTwo = new History(userOne.getId(), "약이름2");
        historyService.join(hisOne);
        historyService.join(hisTwo);
        String pillName = "약이름1";
        historyService.removeTakePill(userOne.getId(),pillName);
        List<History> takePill = historyService.findTakePill(userOne.getId());
        assertThat(takePill.size()).isEqualTo(1);
    }

    @DisplayName("유저 복용 이력 삭제 실패")
    @Test
    void 복용이력_삭제실패(){
        User userOne = new User("ajou123", "hello1@naver.com", "12345678");
        userService.join((userOne));
        History hisOne = new History(userOne.getId(), "약이름1");
        History hisTwo = new History(userOne.getId(), "약이름2");
        historyService.join(hisOne);
        historyService.join(hisTwo);
        String pillName = "약이름3";
        historyService.removeTakePill(userOne.getId(),pillName);
        List<History> takePill = historyService.findTakePill(userOne.getId());
        assertThat(takePill.size()).isEqualTo(2);
    }
}
