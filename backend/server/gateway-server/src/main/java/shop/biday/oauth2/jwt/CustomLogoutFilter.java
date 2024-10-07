package shop.biday.oauth2.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import shop.biday.utils.RedisTemplateUtils;


@Component
public class CustomLogoutFilter implements WebFilter {

    private final JWTUtil jwtUtil;
    private final RedisTemplateUtils<String> redisTemplateUtils;

    public CustomLogoutFilter(JWTUtil jwtUtil, RedisTemplateUtils<String> redisTemplateUtils) {
        this.jwtUtil = jwtUtil;
        this.redisTemplateUtils = redisTemplateUtils;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String requestUri = exchange.getRequest().getURI().getPath();

        if (!requestUri.equals("/logout")) {
            return chain.filter(exchange);
        }

        if (!exchange.getRequest().getMethod().equals(HttpMethod.POST)) {
            exchange.getResponse().setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
            return exchange.getResponse().setComplete();
        }

        MultiValueMap<String, HttpCookie> cookies = exchange.getRequest().getCookies();
        HttpCookie refreshCookie = cookies.getFirst("refresh");
        if (refreshCookie == null) {
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().setComplete();
        }

        String refresh = refreshCookie.getValue();

        if (refresh == null || refresh.isEmpty()) {
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().setComplete();
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().setComplete();
        }

        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().setComplete();
        }


        String id = jwtUtil.getId(refresh);
        if (!redisTemplateUtils.existsKey(id)) {
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().setComplete();
        }

        deleteRefreshToken(id);

        ResponseCookie cookie = ResponseCookie.from("refresh", null)
                .maxAge(0)
                .path("/")
                .build();
        exchange.getResponse().addCookie(cookie);
        exchange.getResponse().setStatusCode(HttpStatus.OK);
        return exchange.getResponse().setComplete();
    }

    private void deleteRefreshToken(String id) {
        redisTemplateUtils.delete(id);
    }
}
