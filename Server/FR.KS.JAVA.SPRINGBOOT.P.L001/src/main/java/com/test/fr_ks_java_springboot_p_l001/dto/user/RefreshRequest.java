package com.test.fr_ks_java_springboot_p_l001.dto.user;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(
        @NotBlank String refreshToken
) {}