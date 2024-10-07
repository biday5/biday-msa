package shop.biday.oauth2.jwt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class LoginFilter extends AuthenticationWebFilter {

    private final ReactiveAuthenticationManager authenticationManager;
    private final LoginSuccessHandler loginSuccessHandler;

    public LoginFilter(ReactiveAuthenticationManager authenticationManager, LoginSuccessHandler loginSuccessHandler) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.loginSuccessHandler = loginSuccessHandler;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (exchange.getRequest().getURI().getPath().equals("/login")) {
            return authenticate(exchange, chain);
        }
        return chain.filter(exchange);
    }

public Mono<Void> authenticate(ServerWebExchange exchange, WebFilterChain chain) {
    return extractLoginData(exchange)
            .flatMap(loginData -> authenticateUser(loginData))
            .flatMap(authenticationResult ->
                    loginSuccessHandler.successfulAuthentication(exchange, authenticationResult)
                            .then(Mono.just(1)) // 성공 후 1 반환
            )
            .doOnError(e -> unsuccessfulAuthentication(exchange, e))
            .then();
}


    private Mono<Map<String, String>> extractLoginData(ServerWebExchange exchange) {
        return exchange.getRequest().getBody()
                .next()
                .flatMap(dataBuffer -> {
                    String requestBody = dataBuffer.toString(StandardCharsets.UTF_8);
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        return Mono.just(objectMapper.readValue(requestBody, new TypeReference<Map<String, String>>() {
                        }));
                    } catch (IOException e) {
                        return Mono.error(new IllegalArgumentException("요청 본문을 파싱하는 중 오류가 발생했습니다."));
                    }
                })
                .flatMap(loginData -> {
                    String email = loginData.get("username");
                    String password = loginData.get("password");

                    if (email == null || password == null) {
                        return Mono.error(new IllegalArgumentException("올바른 사용자 이름과 비밀번호를 입력해야 합니다."));
                    }

                    return Mono.just(loginData);
                });
    }

    private Mono<Authentication> authenticateUser(Map<String, String> loginData) {

        String email = loginData.get("username");
        String password = loginData.get("password");
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);

        return authenticationManager.authenticate(authToken);
    }

    private void unsuccessfulAuthentication(ServerWebExchange exchange, Throwable error) {
        log.warn("Authentication failed: {}", error.getMessage());
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
    }
}
