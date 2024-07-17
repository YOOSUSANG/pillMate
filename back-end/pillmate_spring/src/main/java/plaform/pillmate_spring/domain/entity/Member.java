package plaform.pillmate_spring.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String username;
    private String profileImgUrl;
    private String nickName;
    private String name;
    private String password;
    private String gender;
    private String email;
    private String role;

    @OneToMany(fetch = LAZY, mappedBy = "member")
    private final List<Take> takes = new ArrayList<>();

    // ***** 주입 메서드 *****
    public void changeNickname(String nickName) {
        this.nickName = nickName;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    public void changeRole(String role) {
        this.role = role;
    }


    public void changeGender(String gender) {
        this.gender = gender;
    }

    public void changeProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }


    // ***** 생성 메서드 *****
    public static Member createMemberByOAuth(String username, String name, String nickname, String email, String profileImgUrl, String gender, String role) {
        Member member = Member.builder()
                .username(username)
                .name(name)
                .nickName(nickname)
                .profileImgUrl(profileImgUrl)
                .gender(gender)
                .password("default")
                .role(role)
                .email(email).build();
        return member;
    }

    // ***** 수정 메서드 *****
    public static void initMember(Member member, String nickName, String gender) {
        member.changeNickname(nickName);
        member.changeGender(gender);
    }

    public static void updateMemberByOAuth(Member member, String name, String nickName, String email, String gender, String role) {
        member.changeNickname(name);
        member.changeEmail(email);
        member.changeRole(role);
        member.changeNickname(nickName);
        member.changeGender(gender);
    }

}
