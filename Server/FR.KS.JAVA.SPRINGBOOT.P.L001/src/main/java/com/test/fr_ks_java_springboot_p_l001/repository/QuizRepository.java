package com.test.fr_ks_java_springboot_p_l001.repository;

import com.test.fr_ks_java_springboot_p_l001.entity.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, UUID>, JpaSpecificationExecutor<Quiz> {

    @EntityGraph(attributePaths = {"questions", "questions.answers"})
    Optional<Quiz> findWithQuestionsAndAnswersById(UUID id);

    // list có thể không cần answers, tùy use-case
    @EntityGraph(attributePaths = {"questions"})
    Page<Quiz> findAllByActiveTrue(Pageable pageable);

    Optional<Quiz> findByIdAndActiveTrue(UUID id);

    boolean existsByTitle(String title);
}
