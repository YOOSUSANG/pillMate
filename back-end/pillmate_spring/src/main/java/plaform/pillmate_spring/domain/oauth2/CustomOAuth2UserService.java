package plaform.pillmate_spring.domain.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import plaform.pillmate_spring.domain.dto.MemberLoginOAuth;
import plaform.pillmate_spring.domain.dto.oauth2.GoogleResponse;
import plaform.pillmate_spring.domain.dto.oauth2.KakaoResponse;
import plaform.pillmate_spring.domain.dto.oauth2.NaverResponse;
import plaform.pillmate_spring.domain.dto.oauth2.OAuth2Response;
import plaform.pillmate_spring.domain.service.MemberService;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;

    //oauth2 로그인 성공 후 실행되는 코드
    @SneakyThrows
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("OAuth2LoginAuthenticationFilter 실행");
        // 액세스 토큰이 들어있느 정보(userRequest)로 유저 정보 획득

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        log.info("Attribute 확인 : {}", oAuth2User.getAttributes());

        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());

        } else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }
        // username unique value
        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        String initProfile = "https://pillmate-s3-1.s3.ap-northeast-2.amazonaws.com/pandalogo.png";
        MemberLoginOAuth memberOauth2Dto = MemberLoginOAuth.builder()
                .email(oAuth2Response.getEmail())
                .username(username)
                .name(oAuth2Response.getName())
                .nickname(oAuth2Response.getNickname())
                .profileImageUrl(initProfile)
                .gender(oAuth2Response.getGender())
                .role("ROLE_USER")
                .build();
        try {
            memberService.join(memberOauth2Dto);

        } catch (Exception e) {
            memberService.dataUpdate(memberOauth2Dto);
        }

        // 스프링 시큐리티 세션에 저장 -> 강제가 아니고 자연스러운 흐름이다. <<권한 이용 가능>>
        return new CustomOAuth2User(memberOauth2Dto);
    }

}
