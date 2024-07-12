package plaform.pillmate_spring.domain.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import plaform.pillmate_spring.domain.dto.MemberLoginOAuth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final MemberLoginOAuth memberLoginOAuth;

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return memberLoginOAuth.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getName() {
        return memberLoginOAuth.getName();
    }

    public String getUsername() {
        return memberLoginOAuth.getUsername();
    }
}
