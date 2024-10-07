package shop.biday.oauth2.social;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.stereotype.Component;

@Component
public class SocialClientRegistration {

    public ClientRegistration naverClientRegistration() {

        return ClientRegistration.withRegistrationId("naver")
                .clientId("RD1oMcYETTxoiJLIsYYS")
                .clientSecret("2MM83u4Q5k")
                .redirectUri("http://localhost:8000/login/oauth2/code/naver")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .scope("name", "email","mobile")
                .authorizationUri("https://nid.naver.com/oauth2.0/authorize")
                .tokenUri("https://nid.naver.com/oauth2.0/token")
                .userInfoUri("https://openapi.naver.com/v1/nid/me")
                .userNameAttributeName("response")
                .build();
    }

}
