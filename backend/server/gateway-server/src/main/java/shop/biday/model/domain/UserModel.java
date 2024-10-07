package shop.biday.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import shop.biday.model.enums.Role;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private String id;
    private String oauthName;
    private String name;
    private String email;
    private String password;
    private String phoneNum;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
   // private Role role;
    private String status;
    private Long totalRating;

    private String newPassword;

    private List<Role> role;

    public UserModel(String id, String oauthName, String name, String email, String roleAsString) {
        this.id = id;
        this.oauthName = oauthName;
        this.name = name;
        this.email = email;
        this.role = Collections.singletonList(Role.valueOf(roleAsString));
    }

//    public String getRoleAsString() {
//        return role != null ? role.getAuthority() : "";
//    }

}
