package shop.biday.oauth2.social;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;

@Configuration
@RequiredArgsConstructor
public class CustomClientRegistrationRepo {

    private final SocialClientRegistration socialClientRegistration;

    @Bean
    public ReactiveClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryReactiveClientRegistrationRepository(socialClientRegistration.naverClientRegistration());
    }
}