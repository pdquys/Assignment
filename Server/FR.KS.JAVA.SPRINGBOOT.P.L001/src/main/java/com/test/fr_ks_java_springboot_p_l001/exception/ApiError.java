package com.test.fr_ks_java_springboot_p_l001.exception;

import java.time.Instant;

public record ApiError (
        Instant timestamp,
        int status,
        String error,
        String message,
        String path
) {}