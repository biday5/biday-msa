package shop.biday.oauth2.OauthDto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import shop.biday.model.domain.UserModel;
import shop.biday.model.enums.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final UserModel userModel;

    public CustomOAuth2User(UserModel userModel) {
        this.userModel = userModel;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null; // 필요한 경우 UserDocument의 속성을 반환하도록 구현
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : userModel.getRole()) {
            authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }
        return authorities;
    }

    @Override
    public String getName() {
        return userModel.getName();
    }

    public String getOauthName() {
        return userModel.getOauthName();
    }

    public String getEmail() {
        return userModel.getEmail();
    }

    public String getId() {
        return userModel.getId();
    }
}

