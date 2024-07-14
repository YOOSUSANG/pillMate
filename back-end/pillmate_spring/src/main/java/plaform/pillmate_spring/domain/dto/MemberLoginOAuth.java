package plaform.pillmate_spring.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberLoginOAuth {

    private String username;
    private String name;
    private String password;
    private String profileImageUrl;
    private String email;
    private String gender;
    private String nickname;
    private String role;

}
