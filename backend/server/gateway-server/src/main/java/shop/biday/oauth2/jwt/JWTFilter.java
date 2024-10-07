package shop.biday.oauth2.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;
import shop.biday.model.domain.UserModel;
import shop.biday.model.enums.Role;
import shop.biday.oauth2.OauthDto.CustomOAuth2User;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter implements WebFilter {

    private final JWTUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String accessToken = authorizationHeader.substring(7);

        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete(); // 응답 종료
        }

        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals("access")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().writeWith(Mono.just(
                    exchange.getResponse().bufferFactory().wrap("유효하지 않은 액세스 토큰입니다.".getBytes(StandardCharsets.UTF_8))
            ));
        }

        String id = jwtUtil.getId(accessToken);
        String roleString = jwtUtil.getRole(accessToken);
        String name = jwtUtil.getName(accessToken);

        Role role;
        try {
            role = Role.fromString(roleString);
        } catch (IllegalArgumentException e) {
            log.error("error message : {}", e.getMessage());
            return chain.filter(exchange);
        }

        UserModel userModel = new UserModel();
        userModel.setId(id);
        userModel.setRole(Collections.singletonList(role));
        userModel.setName(name);

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userModel);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());

        ServerHttpRequest request = exchange.getRequest().mutate().build();

        // 업데이트된 요청으로 exchange를 재구성
        ServerWebExchange mutatedExchange = exchange.mutate().request(request).build();


        return chain.filter(mutatedExchange)
                .contextWrite(Context.of(Authentication.class, authToken));

    }

}
