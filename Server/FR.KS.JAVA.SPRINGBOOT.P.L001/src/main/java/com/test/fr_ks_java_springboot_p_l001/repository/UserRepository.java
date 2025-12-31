package com.test.fr_ks_java_springboot_p_l001.repository;

import com.test.fr_ks_java_springboot_p_l001.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByIdAndActiveTrue(UUID id);

    Page<User> findByActiveTrue(Pageable pageable);

    boolean existsByEmailIgnoreCase(String email);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmailIgnoreCaseAndActiveTrue(String email);
}
