package plaform.pillmate_spring.domain.dto.oauth2;

public interface OAuth2Response {

    String getProvider();

    String getProviderId();

    String getEmail();

    String getName();

    String getNickname();

    String getGender();


}
