package plaform.pillmate_spring;


import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import plaform.pillmate_spring.domain.User;
import plaform.pillmate_spring.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class UserTest {
    @Autowired
    UserService userService;

    @BeforeEach
    void 시작후_초기화() {
        userService.removeAllUser();
    }

    @DisplayName("회원 가입")
    @Test
    void 회원가입() {
        User userOne = new User("ajou123", "hello1@naver.com", "12345678");
        User userTwo = new User("ajou1234", "hello2@naver.com", "12345678");

        userService.join((userOne));
        userService.join((userTwo));

        List<User> users = userService.findAllUser();
        assertThat(users.size()).isEqualTo(2);
    }

    @DisplayName("로그인")
    @Test
    void 로그인() {
        User userOne = new User("ajou123", "hello1@naver.com", "12345678");
        User userTwo = new User("ajou1234", "hello2@naver.com", "12345678");
        userService.join((userOne));
        userService.join((userTwo));
        String logEmail = "hello1@naver.com";
        Optional<User> user = userService.findUser(logEmail,"12345678");
        assertThat(user.get()).isEqualTo(userOne);
    }

    @DisplayName("로그인 정보 없음")
    @Test
    void 로그인_정보없음() {
        User userOne = new User("ajou123", "hello1@naver.com", "12345678");
        User userTwo = new User("ajou1234", "hello2@naver.com", "12345678");
        userService.join((userOne));
        userService.join((userTwo));
        String logEmail = "hello3@naver.com";
        Optional<User> user = userService.findUser(logEmail,"12345678");
        //현재 Optional에 값이 없으므로 isPresent()는 false이다.
        assertThat(user.isPresent()).isFalse();
    }



}
