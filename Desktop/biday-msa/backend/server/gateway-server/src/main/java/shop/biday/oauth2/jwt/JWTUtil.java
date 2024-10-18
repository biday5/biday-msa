package shop.biday.oauth2.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import shop.biday.exception.auth.JwtAuthenticationException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JWTUtil {

    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }


    public JwtClaims extractClaims(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String tokens = token.substring(7);
            return validateJwtToken(tokens);
        }
        return null;
    }

    public JwtClaims validateJwtToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token); // Get claims
            String userId = claims.get("id", String.class);
            String role = claims.get("role", String.class);
            String name = claims.get("name", String.class);
            return new JwtClaims(userId, role, name); // Return claims encapsulated in JwtClaims object
        } catch (JwtException e) {
            log.error("JWT validation failed: {}", e.getMessage());
            return null; // Return null if validation fails
        }
    }

    public String getCategory(String token) {
        return getClaimFromToken(token, "category"); // 공통 메서드로 변경
    }

    public String getId(String token) {
        return getClaimFromToken(token, "id"); // 공통 메서드로 변경
    }

    public String getRole(String token) {
        return getClaimFromToken(token, "role"); // 공통 메서드로 변경
    }

    public String getName(String token) {
        return getClaimFromToken(token, "name"); // 공통 메서드로 변경
    }

    public Boolean isExpired(String token) {
        try {
            //return getClaimsFromToken(token).getExpiration().before(new Date());
            Date expiration = getClaimsFromToken(token).getExpiration();
            return expiration.before(new Date());
        } catch (JwtException e) {
            log.error("Token verification failed: {}", e.getMessage()); // 로깅 추가
            return true; // 예외 발생 시 만료된 것으로 간주
        }
    }

    private String getClaimFromToken(String token, String claimKey) {
        Claims claims = getClaimsFromToken(token); // 재사용 가능성을 위한 공통 메서드
        return claims.get(claimKey, String.class);
    }

    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            log.error("Failed to parse JWT: {}", e.getMessage());
            throw new JwtAuthenticationException("JWT token 유효하지 않습니다.");
        }
    }

    public String createJwt(String category, String id, String role, String name, Long expiredMs) {
        return Jwts.builder()
                .claim("category", category)
                .claim("id", id)
                .claim("role", role)
                .claim("name", name)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}