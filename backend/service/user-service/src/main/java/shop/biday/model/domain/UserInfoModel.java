package shop.biday.model.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.stereotype.Component;
import shop.biday.model.enums.Role;

@Builder
@Component
public class UserInfoModel {
    private String userId;
    private String userName;
    private String userRole;

    // 기본 생성자는 Jackson에서 직렬화를 위해 필요합니다.
    public UserInfoModel() {}

    // @JsonCreator와 @JsonProperty를 사용하여 생성자를 통해 값을 설정
    @JsonCreator
    public UserInfoModel(
            @JsonProperty("userId") String userId,
            @JsonProperty("userName") String userName,
            @JsonProperty("userRole") String userRole) {
        this.userId = userId;
        this.userName = userName;
        this.userRole = userRole;
    }

    @JsonProperty("userId") // JSON 직렬화 시 필드 이름을 지정
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonProperty("userName")
    public String getUserName() {
        return userName;
    }


    @JsonProperty("userRole")
    public String getUserRole() {  // 변경된 타입
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = String.valueOf(userRole);
    }

}
