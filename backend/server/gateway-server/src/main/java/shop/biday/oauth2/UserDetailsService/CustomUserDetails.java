package shop.biday.oauth2.UserDetailsService;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import shop.biday.model.domain.UserModel;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final UserModel UserModel;

    public CustomUserDetails(UserModel UserModel) {
        this.UserModel = UserModel;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return UserModel.getRole().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return UserModel.getPassword();
    }

    @Override
    public String getUsername() {
        return null;
    }

    public String getName() {
        return UserModel.getName();
    }

    public String getEmail() {
        return UserModel.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getId() {
        return UserModel.getId();
    }
}
