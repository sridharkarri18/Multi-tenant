package com.project.multitenant.services.impl;

import com.project.multitenant.dto.AuthResponse;
import com.project.multitenant.dto.LoginRequest;
import com.project.multitenant.dto.SignupRequest;
import com.project.multitenant.entities.Organization;
import com.project.multitenant.entities.Role;
import com.project.multitenant.entities.User;
import com.project.multitenant.exceptions.ResourceNotFoundException;
import com.project.multitenant.repositories.OrganizationRepository;
import com.project.multitenant.repositories.UserRepository;
import com.project.multitenant.security.JwtUtil;
import com.project.multitenant.security.UserPrincipal;
import com.project.multitenant.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private UserRepository userRepository;
    @Autowired private OrganizationRepository organizationRepository;
    @Autowired private PasswordEncoder encoder;
    @Autowired private JwtUtil jwtUtil;

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateJwtToken(authentication);
        
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();

        return new AuthResponse(jwt, userDetails.getRole().name());
    }

    @Override
    @Transactional
    public void signupOrgAdmin(SignupRequest request) {
        registerUser(request, Role.ORG_ADMIN);
    }

    @Override
    @Transactional
    public void signupEndUser(SignupRequest request) {
        registerUser(request, Role.END_USER);
    }

    private void registerUser(SignupRequest request, Role role) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Error: Email is already in use!");
        }

        Organization org = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(role);
        user.setOrganization(org);

        userRepository.save(user);
    }
}
