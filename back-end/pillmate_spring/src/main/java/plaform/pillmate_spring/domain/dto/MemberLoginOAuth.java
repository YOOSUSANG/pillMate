package plaform.pillmate_spring.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "OAuth2 login 후 회원가입 DTO")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberLoginOAuth {

    @Schema(description = "OAuth username", defaultValue = "ex) kakao 21321312423, google 241243214231")
    private String username;
    @Schema(description = "회원 이름",defaultValue = "ex) 이무개, 홍길동")
    private String name;
    @Schema(description = "회원 비밀번호")
    private String password;
    @Schema(description = "회원 프로필 경로")
    private String profileImageUrl;
    @Schema(description = "회원 이메일",defaultValue = "ex) hello@naver.com")
    private String email;
    @Schema(description = "회원 성별",defaultValue = "M or F")
    private String gender;
    @Schema(description = "회원 닉네임",defaultValue = "ex) 필메이트킹")
    private String nickname;
    @Schema(description = "회원 권한", defaultValue = "ex) user, admin")
    private String role;

}
