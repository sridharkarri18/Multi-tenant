package com.project.multitenant.security;

import com.project.multitenant.entities.Role;
import com.project.multitenant.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final Role role;
    private final Long organizationId;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long id, String email, String password, Role role, Long organizationId, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.organizationId = organizationId;
        this.authorities = authorities;
    }

    public static UserPrincipal build(User user) {
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                user.getOrganization() != null ? user.getOrganization().getId() : null,
                Collections.singletonList(authority)
        );
    }

    public Long getId() { return id; }
    public Role getRole() { return role; }
    public Long getOrganizationId() { return organizationId; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override
    public String getPassword() { return password; }
    @Override
    public String getUsername() { return email; }
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}
