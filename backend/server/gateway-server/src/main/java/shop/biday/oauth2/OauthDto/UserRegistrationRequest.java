package shop.biday.oauth2.OauthDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import shop.biday.model.enums.Role;

import java.time.LocalDateTime;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {
    private String oauthName;
    private String name;
    private String email;
    private String password;
    private String phoneNum;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private Role role;
    private boolean status;
    private double totalRating;
}
