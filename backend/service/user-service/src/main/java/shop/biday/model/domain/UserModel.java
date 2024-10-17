package shop.biday.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import shop.biday.model.document.UserDocument;
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
    private boolean status;
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

    public static UserModel fromDocument(UserDocument userDocument) {
        return UserModel.builder()
                .id(userDocument.getId())
                .oauthName(userDocument.getOauthUser())
                .name(userDocument.getName())
                .email(userDocument.getEmail())
                .password(userDocument.getPassword())
                .phoneNum(userDocument.getPhone())
                .createAt(userDocument.getCreatedAt())
                .updateAt(userDocument.getUpdatedAt())
                .status(userDocument.isStatus()) // boolean 값을 그대로 사용
                .totalRating((long) userDocument.getTotalRating())
                .role(userDocument.getRole())
                .build();
    }

}
