package shop.biday.model.document;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import shop.biday.model.enums.Role;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    @Field("email")
    private String email;

    @Field("password")
    private String password;

    @Field("name")
    private String name;

    @Field("oauthUser")
    private String oauthUser;

    @Indexed(unique = true)
    @Field("phone_number")
    private String phone;

    @CreatedDate
    @Field(write = Field.Write.ALWAYS)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field
    private LocalDateTime updatedAt;

    @Field("roles")
    @Builder.Default
    private List<Role> role = new ArrayList<>(List.of(Role.ROLE_USER)); // Default role


    @Field("status")
    @Builder.Default
    private boolean status = true; // 기본값을 true로 설정

    @Field("total_rating")
    @Builder.Default
    private double totalRating = 2.0; // 기본값 설정



//     추가된 메서드
//    public List<String> getRoles() {
//        return Collections.singletonList(role.name()); // 단일 역할을 리스트로 반환
//    }
//    public List<String> getRoles() {
//        List<String> roles = new ArrayList<>();
//        roles.add(role.name()); // Add the single role
//        return roles; // Return a mutable list
//    }
}
