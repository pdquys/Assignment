package com.test.fr_ks_java_springboot_p_l001.repository;

import com.test.fr_ks_java_springboot_p_l001.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnswerRepository extends JpaRepository<Answer, UUID> {
}
