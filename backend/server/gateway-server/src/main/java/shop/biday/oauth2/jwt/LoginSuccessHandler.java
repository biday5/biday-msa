package shop.biday.oauth2.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import shop.biday.model.domain.LoginHistoryModel;
import shop.biday.oauth2.UserDetailsService.CustomUserDetails;
import shop.biday.utils.RedisTemplateUtils;


@Slf4j
public class LoginSuccessHandler {

    private static final long ACCESS_TOKEN_EXPIRY_MS = 600000L; // 10 minutes
    private static final long REFRESH_TOKEN_EXPIRY_MS = 86400000L; // 1 day
    private static final int COOKIE_MAX_AGE_SECONDS = 24 * 60 * 60; // 1 day
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";

    private final WebClient webClient;
    private final JWTUtil jwtUtil;
    private final RedisTemplateUtils<String> redisTemplateUtils;

    public LoginSuccessHandler(JWTUtil jwtUtil, RedisTemplateUtils<String> redisTemplateUtils, WebClient webClient) {
        this.jwtUtil = jwtUtil;
        this.redisTemplateUtils = redisTemplateUtils;
        this.webClient = webClient;
    }

    public Mono<Void> successfulAuthentication(ServerWebExchange exchange, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String name = customUserDetails.getName();
        String id = customUserDetails.getId();
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("");

        String access = jwtUtil.createJwt(ACCESS_TOKEN_TYPE, id, role, name, ACCESS_TOKEN_EXPIRY_MS);
        String refresh = jwtUtil.createJwt(REFRESH_TOKEN_TYPE, id, role, name, REFRESH_TOKEN_EXPIRY_MS);

        log.info("Saving refresh token: id = {}, refresh = {}, expiry = {}", id, refresh, REFRESH_TOKEN_EXPIRY_MS);

        return addRefreshEntity(id, refresh, REFRESH_TOKEN_EXPIRY_MS)
                .then(saveLoginHistory(id))
                .then(Mono.defer(() -> {
                    exchange.getResponse().getHeaders().add("Authorization", "Bearer " + access);
                    exchange.getResponse().addCookie(createCookie("refresh", refresh));
                    exchange.getResponse().setStatusCode(HttpStatus.OK);
                    return exchange.getResponse().setComplete();
                }))
                .onErrorResume(e -> {
                    log.error("Error during authentication process", e);
                    exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                    return exchange.getResponse().setComplete();
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
