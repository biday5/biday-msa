package shop.biday.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_USER("ROLE_USER"),
    ROLE_SELLER("ROLE_SELLER");

    private final String role;

    public String getAuthority() {
        return role;
    }

    // 문자열을 Role 열거형으로 변환하는 유틸리티 메소드
    public static Role fromString(String roleString) {
        for (Role role : Role.values()) {
            if (role.getRole().equals(roleString)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No enum constant for role: " + roleString);
    }
}