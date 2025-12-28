package com.test.fr_ks_java_springboot_p_l001.repository;

import com.test.fr_ks_java_springboot_p_l001.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface questionRepository extends JpaRepository<Question, UUID> {

    Page<Question> findByActiveTrue(Pageable pageable);

    Optional<Question> findByIdAndActiveTrue(UUID id);

    boolean existsByIdAndActiveTrue(UUID id);
}
