package com.project.multitenant.configuration;

import com.project.multitenant.entities.Role;
import com.project.multitenant.entities.User;
import com.project.multitenant.repositories.OrganizationRepository;
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
    private OrganizationRepository organizationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByEmail("superadmin@example.com")) {
            User superAdmin = new User();
            superAdmin.setEmail("superadmin@example.com");
            superAdmin.setPassword(passwordEncoder.encode("admin123"));
            superAdmin.setRole(Role.SUPER_ADMIN);
            superAdmin.setOrganization(null);
            userRepository.save(superAdmin);
        }

        if (!userRepository.existsByEmail("admin@netflix.com")) {
            // Create a default organization
            com.project.multitenant.entities.Organization org = new com.project.multitenant.entities.Organization();
            org.setName("Netflix");
            org = organizationRepository.save(org);

            // Create Org Admin
            User orgAdmin = new User();
            orgAdmin.setEmail("admin@netflix.com");
            orgAdmin.setPassword(passwordEncoder.encode("pass123"));
            orgAdmin.setRole(Role.ORG_ADMIN);
            orgAdmin.setOrganization(org);
            userRepository.save(orgAdmin);

            // Create End User
            User endUser = new User();
            endUser.setEmail("user@netflix.com");
            endUser.setPassword(passwordEncoder.encode("pass123"));
            endUser.setRole(Role.END_USER);
            endUser.setOrganization(org);
            userRepository.save(endUser);

            System.out.println("=================================================");
            System.out.println("TEST CREDENTIALS SEEDED:");
            System.out.println("Super Admin: superadmin@example.com / admin123");
            System.out.println("Org Admin  : admin@netflix.com / pass123");
            System.out.println("End User   : user@netflix.com / pass123");
            System.out.println("=================================================");
        }
    }
}
