package shop.biday.oauth2.UserDetailsService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import shop.biday.model.domain.UserModel;


@Slf4j
public class  CustomReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final WebClient webClient;
    private final PasswordEncoder passwordEncoder;


    public CustomReactiveAuthenticationManager(WebClient webClient, PasswordEncoder passwordEncoder) {
        this.webClient = webClient;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Mono<Authentication> authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        return webClient.get()
                .uri("http://localhost:9106/api/users/oauthLogin/{email}", email)
                .retrieve()
                .bodyToMono(UserModel.class)
                .flatMap(userDocument -> {
                    if (userDocument != null) {
                        if (isPasswordValid(password, userDocument.getPassword())) {
                            CustomUserDetails customUserDetails = new CustomUserDetails(userDocument);
                            return Mono.just((Authentication) new UsernamePasswordAuthenticationToken(customUserDetails, password, customUserDetails.getAuthorities()));
                        } else {
                            return Mono.error(new IllegalArgumentException("비밀번호가 일치하지 않습니다."));
                        }
                    } else {
                        return Mono.error(new IllegalArgumentException("사용자를 찾을 수 없습니다."));
                    }
                })
                .doOnError(e -> log.error("인증 중 오류 발생: {}", e.getMessage()));
    }


    private boolean isPasswordValid(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}