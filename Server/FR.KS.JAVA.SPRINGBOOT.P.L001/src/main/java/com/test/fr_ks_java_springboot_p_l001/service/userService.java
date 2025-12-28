package com.test.fr_ks_java_springboot_p_l001.service;

import com.test.fr_ks_java_springboot_p_l001.dto.PageResponseDTO;
import com.test.fr_ks_java_springboot_p_l001.dto.user.UserRequest;
import com.test.fr_ks_java_springboot_p_l001.dto.user.UserResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface userService {

    UserResponse create(UserRequest req);

    PageResponseDTO<UserResponse> getAll(Pageable pageable);

    UserResponse getById(UUID id);

    UserResponse update(UUID id, UserRequest req);

    void delete(UUID id);
}
