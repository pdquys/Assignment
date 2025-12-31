package com.test.fr_ks_java_springboot_p_l001.service;

import com.test.fr_ks_java_springboot_p_l001.dto.user.AuthResponse;
import com.test.fr_ks_java_springboot_p_l001.dto.user.LoginRequest;
import com.test.fr_ks_java_springboot_p_l001.dto.user.RefreshRequest;
import com.test.fr_ks_java_springboot_p_l001.dto.user.RegisterRequest;

public interface AuthService {

    void register(RegisterRequest req);

    AuthResponse login(LoginRequest req);

    AuthResponse refresh(RefreshRequest req);
}
