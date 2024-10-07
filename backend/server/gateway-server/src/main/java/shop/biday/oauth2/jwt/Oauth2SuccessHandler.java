package shop.biday.oauth2.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import shop.biday.model.domain.LoginHistoryModel;
import shop.biday.oauth2.OauthDto.CustomOAuth2User;
import shop.biday.utils.RedisTemplateUtils;

import java.net.URI;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2SuccessHandler implements ServerAuthenticationSuccessHandler {

    private static final long ACCESS_TOKEN_EXPIRY_MS = 600000L; // 10 minutes
    private static final long REFRESH_TOKEN_EXPIRY_MS = 86400000L; // 1 day
    private static final int COOKIE_MAX_AGE_SECONDS = 24 * 60 * 60; // 1 day
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";
    private static final String REDIRECT_URL = "http://localhost:3000/";
    // 이걸 바꿔보라고 하심 리스폰 값으로 바꿔주는게 아니라 스테이스로 내려준잖아.
    // 스테이터스 임시로 페이지를 만들언서 유알엘을 유도를 해봐.
    // 유도를 하잖아. 그러면 백엔드
    // 아무 페이지를 만들어서 거기서 처리를 하고 인덱스로 가지게 처리를 해보자.
    //

    // 걱정인거는 인덱스에서는 로그가 안찍혔잖아.
    // 그러면 똑갈을꺼야. 다른 페이지를 한다고 하더라도,
    // 우리가 중요한 것은 헤더에 쿠키가 있냐 없냐잖아. 토큰이 있냐 없냐인데,

    // 백에서는 주고 있잖아. 형님 말은, 일반 로그인 처럼 리스폰 했을 때 헤더 뭘 하잖아.
    // 무조건 하게 하는거야.
    // 이 토큰이 가질 수 있냐 없냐 이게 문제이다. 이게 못갖고 오는지 이해가 안된다.


    private final JWTUtil jwtUtil;
    private final WebClient webClient;
    private final RedisTemplateUtils<String> redisTemplateUtils;

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String name = customUserDetails.getName();
        String id = customUserDetails.getId();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String access = jwtUtil.createJwt(ACCESS_TOKEN_TYPE, id, role, name, ACCESS_TOKEN_EXPIRY_MS);
        String refresh = jwtUtil.createJwt(REFRESH_TOKEN_TYPE, id, role, name, REFRESH_TOKEN_EXPIRY_MS);

        return addRefreshEntity(id, refresh, REFRESH_TOKEN_EXPIRY_MS)
                .then(saveLoginHistory(id))
                .then(Mono.defer(() -> {
                    var response = webFilterExchange.getExchange().getResponse();
                    response.getHeaders().set("Authorization", "Bearer " + access);
                    response.addCookie(createCookie("refresh", refresh));
                    response.setStatusCode(HttpStatus.FOUND);
                    response.getHeaders().setLocation(URI.create(REDIRECT_URL));
                    return response.setComplete();
                }))
                .onErrorResume(IllegalArgumentException.class, e -> {
                    log.error("Error occurred during authentication success", e);
                    webFilterExchange.getExchange().getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return webFilterExchange.getExchange().getResponse().setComplete();
                });
    }


    private Mono<Void> saveLoginHistory(String userId) {
        return webClient.get()
                .uri("http://localhost:9106/api/loginHistory/{userId}", userId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .doOnNext(exists -> log.info("Login history exists for userId {}: {}", userId, exists))
                .doOnError(error -> log.error("Error during WebClient call: {}", error.getMessage())) // Log errors
                .flatMap(exists -> {
                    if (!exists) {
                        LoginHistoryModel loginHistoryModel = new LoginHistoryModel();
                        loginHistoryModel.setUserId(userId);
                        return webClient.post()
                                .uri("http://localhost:9106/api/loginHistory")
                                .bodyValue(loginHistoryModel)
                                .retrieve()
                                .bodyToMono(LoginHistoryModel.class)
                                .doOnNext(savedLoginHistory -> log.info("Login history saved: {}", savedLoginHistory))
                                .doOnError(error -> log.error("Error saving login history", error))
                                .then();
                    } else {
                        return Mono.empty();
                    }
                })
                .doOnError(error -> log.error("Error checking login history existence", error))
                .then();
    }


    private ResponseCookie createCookie(String key, String value) {
        return ResponseCookie.from(key, value)
                .maxAge(COOKIE_MAX_AGE_SECONDS)
                .path("/")
                .httpOnly(true)
                .secure(true)  // HTTPS에서만 전송
                .sameSite("None")  // Cross-site 요청 허용
                .build();
    }

    private Mono<Void> addRefreshEntity(String id, String refresh, Long expiredMs) {
        return Mono.fromRunnable(() -> {
            try {
                redisTemplateUtils.save(id, refresh, expiredMs);
                log.info("Saved refresh token for userId: {}", id);
            } catch (Exception e) {
                log.error("Error saving refresh token", e);
                throw e;
            }
        }).log().then();
    }
}