package com.test.fr_ks_java_springboot_p_l001.service;

import com.test.fr_ks_java_springboot_p_l001.dto.PageResponseDTO;
import com.test.fr_ks_java_springboot_p_l001.dto.user.UserRequest;
import com.test.fr_ks_java_springboot_p_l001.dto.user.UserResponse;
import com.test.fr_ks_java_springboot_p_l001.entity.Role;
import com.test.fr_ks_java_springboot_p_l001.entity.RoleEnum;
import com.test.fr_ks_java_springboot_p_l001.entity.User;
import com.test.fr_ks_java_springboot_p_l001.exception.NotFoundException;
import com.test.fr_ks_java_springboot_p_l001.repository.RoleRepository;
import com.test.fr_ks_java_springboot_p_l001.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class userServiceImpl implements  userService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserResponse create(UserRequest req) {
        if (userRepository.existsByEmailIgnoreCase(req.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Set<Role> roles = fetchRoles(req.roleIds());

        User u = new User();
        u.setEmail(req.email().toLowerCase().trim());
        u.setPassword(passwordEncoder.encode(req.password()));
        u.setFullName(req.fullName().trim());
        u.setActive(req.active() != null ? req.active() : true);
        u.setRoles(roles);

        return toRes(userRepository.save(u));
    }

    @Override
    public PageResponseDTO<UserResponse> getAll(Pageable pageable) {
        Page<User> page = userRepository.findByActiveTrue(pageable);

        List<UserResponse> items = page.getContent()
                .stream()
                .map(this::toRes)
                .toList();

        return new PageResponseDTO<>(
                items,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    public UserResponse getById(UUID id) {
        User u = userRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
        return toRes(u);
    }

    @Override
    public UserResponse update(UUID id, UserRequest req) {
        User u = userRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));

        // email không cho đổi
        u.setFullName(req.fullName().trim());

        if (req.password() != null && !req.password().isBlank()) {
            u.setPassword(passwordEncoder.encode(req.password()));
        }

        if (req.active() != null) {
            u.setActive(req.active());
        }

        if (req.roleIds() != null && !req.roleIds().isEmpty()) {
            Set<Role> roles = fetchRoles(req.roleIds());
            u.setRoles(roles);
        }

        return toRes(userRepository.save(u));
    }

    @Override
    public void delete(UUID id) {
        User u = userRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
        u.setActive(false);
    }

    private Set<Role> fetchRoles(Set<UUID> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("Role IDs must not be empty");
        }

        List<Role> roles = roleRepository.findByIdIn(roleIds);
        if (roles.size() != roleIds.size()) {
            throw new IllegalArgumentException("One or more roles not found");
        }
        return new HashSet<>(roles);
    }

    private UserResponse toRes(User u) {
        Set<RoleEnum> roleEnums = u.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new UserResponse(
                u.getId(),
                u.getEmail(),
                u.getFullName(),
                u.getActive(),
                roleEnums,
                u.getCreatedAt(),
                u.getUpdatedAt()
        );
    }
}
