package edu.strauteka.example.security;

import edu.strauteka.example.security.configuration.ApplicationUserRole;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static edu.strauteka.example.security.AppUserDetails.USER_DETAILS_VIEW;

@Data
@NoArgsConstructor
@Table(USER_DETAILS_VIEW)
public class AppUserDetails implements UserDetails {
    public static final String USER_DETAILS_VIEW = "APP_USER_DETAILS_VIEW";
    public static final String USER_DETAILS_VIEW_ID = "ID";
    public static final String USER_DETAILS_VIEW_USERNAME = "USERNAME";
    public static final String USER_DETAILS_VIEW_PASSWORD = "PASSWORD";
    public static final String USER_DETAILS_VIEW_AUTHORITIES = "AUTHORITIES";
    public static final String USER_DETAILS_VIEW_ACTIVE = "IS_ACTIVE";
    @Id
    @Column(USER_DETAILS_VIEW_ID)
    private Long id;
    @Column(USER_DETAILS_VIEW_USERNAME)
    private String username;
    @Column(USER_DETAILS_VIEW_PASSWORD)
    private String password;
    //1;2;3...
    @Column(USER_DETAILS_VIEW_AUTHORITIES)
    private String authorities;
    @Column(USER_DETAILS_VIEW_ACTIVE)
    private boolean accountNonExpired = true;
    @Column(USER_DETAILS_VIEW_ACTIVE)
    private boolean accountNonLocked = true;
    @Column(USER_DETAILS_VIEW_ACTIVE)
    private boolean credentialsNonExpired = true;

    @Column(USER_DETAILS_VIEW_ACTIVE)
    private boolean enabled = true;

    public AppUserDetails(String username, String password, String authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public String getPlainAuthorities() {
        return authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Optional.ofNullable(authorities)
                .stream()
                .flatMap(rawAuth -> authorities.lines())
                .map(ApplicationUserRole::of)
                .distinct()
                .flatMap(custom -> custom.getGrantedAuthorities().stream())
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
