package shop.biday.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import shop.biday.service.impl.JWTReissueServiceImpl;


@RestController
@RequiredArgsConstructor
public class JWTReissueController {

    private final JWTReissueServiceImpl jwtReissueServiceImpl;

    @PostMapping("/reissue")
    public Mono<ResponseEntity<Object>> reissue(ServerWebExchange exchange) {
        return jwtReissueServiceImpl.refreshToken(exchange)
                .map(tokens -> {
                    exchange.getResponse().setStatusCode(HttpStatus.OK);
                    exchange.getResponse().getHeaders().add("Authorization", "Bearer " + tokens.get("access"));
                    exchange.getResponse().addCookie(createCookie("refresh", tokens.get("refresh")));
                    return ResponseEntity.ok().build();
                })
                .onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("reissue 토큰 실패"));
    }

    private ResponseCookie createCookie(String key, String value) {
        return ResponseCookie.from(key, value)
                .maxAge(24 * 60 * 60)
                .path("/")
                .httpOnly(true)
                .build();
    }
}
