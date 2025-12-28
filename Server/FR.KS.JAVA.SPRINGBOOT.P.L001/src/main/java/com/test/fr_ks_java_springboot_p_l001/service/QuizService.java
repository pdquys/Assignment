package com.test.fr_ks_java_springboot_p_l001.service;

import com.test.fr_ks_java_springboot_p_l001.dto.PageResponseDTO;
import com.test.fr_ks_java_springboot_p_l001.dto.quiz.AddQuestionsToQuizRequest;
import com.test.fr_ks_java_springboot_p_l001.dto.quiz.QuizRequest;
import com.test.fr_ks_java_springboot_p_l001.dto.quiz.QuizResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface QuizService {

    QuizResponse create(QuizRequest req);

    PageResponseDTO<QuizResponse> getAll(Pageable pageable);

    QuizResponse getById(UUID id);

    QuizResponse update(UUID id, QuizRequest req);

    void delete(UUID id);

    void addQuestions(UUID quizId, java.util.List<UUID> questionIds);
}
