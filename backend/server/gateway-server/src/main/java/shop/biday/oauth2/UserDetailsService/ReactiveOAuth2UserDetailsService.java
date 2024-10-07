package shop.biday.oauth2.UserDetailsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import shop.biday.model.domain.UserModel;
import shop.biday.oauth2.OauthDto.CustomOAuth2User;
import shop.biday.oauth2.OauthDto.NaverResponse;
import shop.biday.oauth2.OauthDto.OAuth2Response;
import shop.biday.oauth2.OauthDto.UserRegistrationRequest;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactiveOAuth2UserDetailsService implements ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final int MINIMUM_AGE = 19;
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ALLOWED_SOCIAL_PROVIDERS = "naver";
    private static final String SERVER_URL = "http://localhost:9106/api/users/register";

    private final WebClient webClient;

    @Override
    public Mono<OAuth2User> loadUser(OAuth2UserRequest userRequest) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        return getOAuth2User(userRequest)
                .flatMap(oAuth2User -> {
                    OAuth2Response oAuth2Response = null;
                    try {
                        if (registrationId.equals(ALLOWED_SOCIAL_PROVIDERS)) {
                            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
                        }

                        if (oAuth2Response != null) {
                            return processUser(oAuth2Response);
                        } else {
                            return Mono.error(new IllegalArgumentException("지원하지 않는 소셜 제공자입니다."));
                        }
                    } catch (IllegalArgumentException e) {
                        return Mono.error(new OAuth2AuthenticationException("잘못된 사용자 정보: " + e.getMessage()));
                    }
                });
    }

    private Mono<OAuth2User> getOAuth2User(OAuth2UserRequest userRequest) {
        WebClient webClient = WebClient.builder().build();

        String userInfoUri = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();
        String accessToken = userRequest.getAccessToken().getTokenValue();

        return webClient.get()
                .uri(userInfoUri)
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .doOnNext(attributes -> log.debug("Received user info: {}", attributes))
                .<OAuth2User>handle((attributes, sink) -> {
                    Object responseObj = attributes.get("response");
                    if (responseObj instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> response = (Map<String, Object>) responseObj;
                        sink.next(new DefaultOAuth2User(
                                Collections.singletonList(new SimpleGrantedAuthority(ROLE_USER)),
                                response,
                                "id"
                        ));
                    } else {
                        sink.error(new IllegalArgumentException("Invalid response format from user info."));
                    }
                })
                .doOnError(ex -> log.error("Error fetching user info: {}", ex.getMessage()))
                .onErrorMap(ex -> {
                    log.error("OAuth2 provider error: {}", ex.getMessage());
                    return new OAuth2AuthenticationException("Failed to fetch user info from OAuth2 provider.");
                });
}


    private Mono<CustomOAuth2User> processUser(OAuth2Response oAuth2Response) {

        if (oAuth2Response == null) {
            return Mono.error(new OAuth2AuthenticationException("OAuth2Response가 빈 값 입니다."));
        }

        String birthYearString = oAuth2Response.getBirthyear();

        if (birthYearString == null || birthYearString.isEmpty()) {
            return Mono.error(new OAuth2AuthenticationException("생년월일 값이 누락되었습니다."));
        }

        try {
            int birthYear = Integer.parseInt(birthYearString);
            int currentYear = java.time.Year.now().getValue();
            int age = currentYear - birthYear;

            if (age < MINIMUM_AGE) {
                return Mono.error(new OAuth2AuthenticationException("사용자는 " + MINIMUM_AGE + "세 미만입니다."));
            }

            String oauthName = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
            return registerUser(oAuth2Response, oauthName);
        } catch (NumberFormatException e) {
            log.error("error message : {}", e.getMessage());
            return Mono.error(new OAuth2AuthenticationException("잘못된 생년 형식입니다: " + e.getMessage()));
        } catch (Exception e) {
            log.error("error message : {}", e.getMessage());
            return Mono.error(new OAuth2AuthenticationException("OAuth2 응답 처리 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    private Mono<CustomOAuth2User> registerUser(OAuth2Response oAuth2Response, String oauthName) {
        UserRegistrationRequest registrationRequest = new UserRegistrationRequest();

            registrationRequest.setEmail(oAuth2Response.getEmail());
            registrationRequest.setOauthName(oauthName);
            registrationRequest.setName(oAuth2Response.getName());
            registrationRequest.setPhoneNum(oAuth2Response.getMobile());

        return webClient.post()
                .uri(SERVER_URL)
                .bodyValue(registrationRequest)
                .retrieve()
                .bodyToMono(UserModel.class)
                .map(CustomOAuth2User::new)
                .onErrorMap(e -> {
                    log.error("회원가입 중 오류 발생: {}", e.getMessage());
                    return new IllegalArgumentException("회원가입에 실패했습니다.");
                });

    }

}
