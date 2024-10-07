package shop.biday.service.impl;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import shop.biday.oauth2.jwt.JWTUtil;
import shop.biday.utils.RedisTemplateUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JWTReissueServiceImpl {

    private static final long ACCESS_TOKEN_EXPIRY_MS = 600000L; // 10 minutes
    private static final long REFRESH_TOKEN_EXPIRY_MS = 86400000L; // 1 day
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";

    private final JWTUtil jwtUtil;
    private final RedisTemplateUtils<String> redisTemplateUtils;

    public Mono<Map<String, String>> refreshToken(ServerWebExchange exchange) {
        String refresh = Optional.ofNullable(exchange.getRequest().getCookies().getFirst("refresh"))
                .map(cookie -> cookie.getValue())
                .orElseThrow(() -> new IllegalArgumentException("리프레시 토큰이 없습니다."));

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            log.error("error message : {}", e.getMessage());
            return Mono.error(new IllegalArgumentException("리프레시 토큰이 만료되었습니다."));
        }

        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            return Mono.error(new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다."));
        }

        String id = jwtUtil.getId(refresh);
        String role = jwtUtil.getRole(refresh);
        String name = jwtUtil.getName(refresh);

        String newAccess = jwtUtil.createJwt(ACCESS_TOKEN_TYPE, id, role, name, ACCESS_TOKEN_EXPIRY_MS);
        String newRefresh = jwtUtil.createJwt(REFRESH_TOKEN_TYPE, id, role, name, REFRESH_TOKEN_EXPIRY_MS);

        deleteRefreshToken(id);
        addRefreshEntity(id, newRefresh, REFRESH_TOKEN_EXPIRY_MS);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access", newAccess);
        tokens.put("refresh", newRefresh);

        return Mono.just(tokens);
    }

    private void addRefreshEntity(String id, String refresh, Long expiredMs)  {
        redisTemplateUtils.save(id, refresh, expiredMs);
    }

    private void deleteRefreshToken(String id) {
        redisTemplateUtils.delete(id);
    }
}
