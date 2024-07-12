package plaform.pillmate_spring.domain.dto.oauth2;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {
    private final Map<String, Object> attriubute;
    private final Map<String, Object> kakaoAccount;

    public KakaoResponse(Map<String, Object> attriubute) {
        this.attriubute = attriubute;
        this.kakaoAccount = (Map<String, Object>) attriubute.get("kakao_account");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attriubute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return kakaoAccount.get("email").toString();
    }

    // 앱 권한 신청 완료시 변경 -> refactor
    @Override
    public String getName() {
        return "true로 변경 필요";
    }

    @Override
    public String getNickname() {
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        return profile.get("nickname").toString();
    }

    // 앱 권한 신청 완료시 변경 -> refactor
    @Override
    public String getGender() {
        return "default";
    }
}
