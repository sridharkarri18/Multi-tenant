package com.project.multitenant.security;

import com.project.multitenant.entities.Role;
import com.project.multitenant.entities.User;
import com.project.multitenant.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    
    @Value("${app.superadmin.email}")
    private String superAdminEmail;
    
    @Value("${app.superadmin.password}")
    private String superAdminPassword;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Intercept Super Admin login
        if (email.equals(superAdminEmail)) {
            User sa = new User();
            sa.setId(0L);
            sa.setEmail(superAdminEmail);
            sa.setPassword(superAdminPassword); // Hashed in SecurityConfig
            sa.setRole(Role.SUPER_ADMIN);
            return UserPrincipal.build(sa);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));

        return UserPrincipal.build(user);
    }
}
