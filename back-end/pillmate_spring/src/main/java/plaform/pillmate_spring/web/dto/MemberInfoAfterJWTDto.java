package plaform.pillmate_spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import plaform.pillmate_spring.domain.entity.Member;

@Data
@Schema(description = "회원 정보 DTO")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberInfoAfterJWTDto {
    @Schema(description = "회원 이름",defaultValue = "ex) 이무개, 홍길동")
    private String name;
    @Schema(description = "회원 이메일",defaultValue = "ex) hello@naver.com")
    private String email;
    @Schema(description = "회원 닉네임",defaultValue = "ex) 필메이트킹")
    private String nickname;
    @Schema(description = "회원 이메일",defaultValue = "ex) hello@naver.com")
    private String profileImageUrl;

    public MemberInfoAfterJWTDto(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
        this.nickname = member.getNickName();
        this.profileImageUrl = member.getProfileImgUrl();
    }
}
