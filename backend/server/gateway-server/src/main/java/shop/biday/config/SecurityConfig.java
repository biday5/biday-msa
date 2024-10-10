package shop.biday.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.session.InMemoryWebSessionStore;
import org.springframework.web.server.session.WebSessionStore;
import shop.biday.oauth2.UserDetailsService.CustomReactiveAuthenticationManager;
import shop.biday.oauth2.jwt.*;
import shop.biday.oauth2.social.CustomClientRegistrationRepo;
import shop.biday.utils.RedisTemplateUtils;

import java.util.Arrays;
import java.util.Collections;


@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomClientRegistrationRepo customClientRegistrationRepo;
    private final Oauth2SuccessHandler oauth2SuccessHandler;
    private final JWTUtil jwtUtil;
    private final RedisTemplateUtils<String> redisTemplateUtils;
    private final PasswordEncoder passwordEncoder;
    private final WebClient webClient;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .addFilterBefore(new JWTFilter(jwtUtil), SecurityWebFiltersOrder.AUTHORIZATION)
                .oauth2Login(oauth2 -> oauth2
                        .clientRegistrationRepository(customClientRegistrationRepo.clientRegistrationRepository())
                        .authorizedClientService(authorizedClientService())
                        .authenticationSuccessHandler(oauth2SuccessHandler))
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/v3/api-docs/**",  "/swagger-ui/**", "/webjars/**").permitAll()
                        .pathMatchers("/actuator/**",  "/*-service/**").permitAll()
                        .pathMatchers("/login","/reissue", "/logout").permitAll()
                        .pathMatchers("/api/faqs/**", "/api/announcements/**").permitAll()
                        .pathMatchers("/api/auctions/**", "/api/awards/**", "/api/bids/**").permitAll()
                        .pathMatchers("/api/images/**").permitAll()
                        .pathMatchers("/api/payments/**","/api/refunds/**","/api/seller-payments/**", "/api/shippers/**").permitAll()
                        .pathMatchers("/api/brands/**", "/api/categories/**", "/api/products/**", "/api/sizes/**", "/api/wishes/**").permitAll()
                        .pathMatchers("/api/sms/**").permitAll()
                        .pathMatchers("/api/account/**", "/api/addresses/**", "/api/loginHistory/**", "/api/users/**").permitAll())
                      // .anyExchange().authenticated())
                .addFilterAt(loginFilter(loginSuccessHandler()), SecurityWebFiltersOrder.AUTHORIZATION)
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, redisTemplateUtils), SecurityWebFiltersOrder.LOGOUT)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance());
        return http.build();
    }


    @Bean
    public WebSessionStore webSessionStore() {
        return new InMemoryWebSessionStore();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "UserInfo"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setMaxAge(3600L);

        // CORS 설정을 경로에 따라 등록
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 CORS 설정 적용

        return source;
    }

    @Bean
    public ReactiveOAuth2AuthorizedClientService authorizedClientService() {
        return new InMemoryReactiveOAuth2AuthorizedClientService(customClientRegistrationRepo.clientRegistrationRepository());
    }

    @Bean
    public LoginFilter loginFilter(LoginSuccessHandler loginSuccessHandler) {
        return new LoginFilter(customReactiveAuthenticationManager(), loginSuccessHandler);
    }

    @Bean
    public CustomReactiveAuthenticationManager customReactiveAuthenticationManager() {
        return new CustomReactiveAuthenticationManager(webClient, passwordEncoder);
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtUtil, redisTemplateUtils, webClient);
    }
}