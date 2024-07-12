package plaform.pillmate_spring;


import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import plaform.pillmate_spring.domain.dto.MemberLoginOAuth;
import plaform.pillmate_spring.domain.entity.Member;
import plaform.pillmate_spring.domain.service.MemberService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberTest {
    @Autowired
    MemberService memberService;
    List<Long> memberIds = new ArrayList<>();
    @BeforeEach
    void 시작후_초기화() throws BadRequestException {
        memberService.removeAllUser();

        MemberLoginOAuth mjd1 = MemberLoginOAuth.builder()
                .username("m1")
                .email("hello@h.com")
                .name("memberOne")
                .password("1234").build();

        MemberLoginOAuth mjd2 = MemberLoginOAuth.builder()
                .username("a1")
                .email("admin@h.com")
                .name("admin")
                .password("1234").build();

        Long joinId1 = memberService.join((mjd1));
        Long joinId2 = memberService.join((mjd2));
        memberIds.add(joinId1);
        memberIds.add(joinId2);
    }

    @DisplayName("회원 가입")
    @Test
    void 회원가입() throws BadRequestException {
        List<Member> members = memberService.findAll();
        assertThat(members.size()).isEqualTo(2);
    }

    @DisplayName("로그인")
    @Test
    void 로그인() throws BadRequestException {
        String logEmail = "hello@h.com";
        Member memberOne = memberService.find(memberIds.get(0));
        Member member = memberService.find(logEmail);
        assertThat(member).isEqualTo(memberOne);
    }

    @DisplayName("로그인 정보 없음")
    @Test
    void 로그인_정보없음() throws BadRequestException {
        String logEmail = "hello3@naver.com";
        Assertions.assertThrows(BadRequestException.class, () -> {
            memberService.find(logEmail);
        });
    }
}
