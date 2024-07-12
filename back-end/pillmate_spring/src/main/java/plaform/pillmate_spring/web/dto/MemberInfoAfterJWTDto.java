package plaform.pillmate_spring.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import plaform.pillmate_spring.domain.entity.Member;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberInfoAfterJWTDto {
    private String name;
    private String email;
    private String nickname;

    public MemberInfoAfterJWTDto(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
        this.nickname = member.getNickName();
    }
}
