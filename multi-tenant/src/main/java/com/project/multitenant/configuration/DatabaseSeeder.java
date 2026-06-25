package com.project.multitenant.configuration;

import com.project.multitenant.entities.Role;
import com.project.multitenant.entities.User;
import com.project.multitenant.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByEmail("superadmin@example.com")) {
            User superAdmin = new User();
            superAdmin.setEmail("superadmin@example.com");
            superAdmin.setPassword(passwordEncoder.encode("admin123"));
            superAdmin.setRole(Role.SUPER_ADMIN);
            superAdmin.setOrganization(null); // Super admin has no specific organization
            userRepository.save(superAdmin);
            System.out.println("=================================================");
            System.out.println("Default Super Admin Created: superadmin@example.com / admin123");
            System.out.println("=================================================");
        }
    }
}
