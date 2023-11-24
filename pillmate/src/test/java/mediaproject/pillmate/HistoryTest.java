package mediaproject.pillmate;

import jakarta.transaction.Transactional;
import mediaproject.pillmate.config.AppConfig;
import mediaproject.pillmate.domain.History;
import mediaproject.pillmate.domain.User;
import mediaproject.pillmate.repository.HistoryJpaRepository;
import mediaproject.pillmate.repository.UserJpaRepository;
import mediaproject.pillmate.service.HistoryService;
import mediaproject.pillmate.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class HistoryTest {
    @Autowired
    HistoryJpaRepository historyJpaRepository;
    @Autowired
    UserJpaRepository userJpaRepository;
    HistoryService historyService;
    UserService userService;

    @BeforeEach
    void 시작전_초기화() {
        AppConfig appConfig = new AppConfig();
        historyService = appConfig.historyService(historyJpaRepository);
        userService = appConfig.userService(userJpaRepository);
    }

    @AfterEach
    void 시작후_초기화() {
        historyService.AllPillRemove();
        userService.allUserRemove();
    }

    @DisplayName("유저 복용 이력 추가")
    @Test
    void 복용이력_추가(){
        User userOne = new User("ajou123", "hello1@naver.com", "12345678");
        userService.join((userOne));
        History hisOne = new History(userOne.getUserId(), "약이름1");
        History hisTwo = new History(userOne.getUserId(), "약이름2");
        historyService.join(hisOne);
        historyService.join(hisTwo);
        List<History> takePill = historyService.findTakePill(userOne.getUserId());
        assertThat(takePill.size()).isEqualTo(2);
    }

    @DisplayName("유저 복용 이력 삭제")
    @Test
    void 복용이력_삭제(){
        User userOne = new User("ajou123", "hello1@naver.com", "12345678");
        userService.join((userOne));
        History hisOne = new History(userOne.getUserId(), "약이름1");
        History hisTwo = new History(userOne.getUserId(), "약이름2");
        historyService.join(hisOne);
        historyService.join(hisTwo);
        String pillName = "약이름1";
        historyService.removeTakePill(userOne.getUserId(),pillName);
        List<History> takePill = historyService.findTakePill(userOne.getUserId());
        assertThat(takePill.size()).isEqualTo(1);
    }

    @DisplayName("유저 복용 이력 삭제 실패")
    @Test
    void 복용이력_삭제실패(){
        User userOne = new User("ajou123", "hello1@naver.com", "12345678");
        userService.join((userOne));
        History hisOne = new History(userOne.getUserId(), "약이름1");
        History hisTwo = new History(userOne.getUserId(), "약이름2");
        historyService.join(hisOne);
        historyService.join(hisTwo);
        String pillName = "약이름3";
        historyService.removeTakePill(userOne.getUserId(),pillName);
        List<History> takePill = historyService.findTakePill(userOne.getUserId());
        assertThat(takePill.size()).isEqualTo(2);
    }
}
