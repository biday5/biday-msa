package shop.biday.oauth2.OauthDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationResponse {
    private String id;
    private String oauthName;
    private String name;
    private String email;
    private List<String> roles;
}
