package com.project.multitenant.services;

import com.project.multitenant.dto.AuthResponse;
import com.project.multitenant.dto.LoginRequest;
import com.project.multitenant.dto.SignupRequest;
import com.project.multitenant.entities.Role;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    void signupOrgAdmin(SignupRequest request);
    void signupEndUser(SignupRequest request);
}
