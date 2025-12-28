package com.test.fr_ks_java_springboot_p_l001.service;

import com.test.fr_ks_java_springboot_p_l001.dto.PageResponseDTO;
import com.test.fr_ks_java_springboot_p_l001.dto.question.questionRequest;
import com.test.fr_ks_java_springboot_p_l001.dto.question.questionResponse;
import com.test.fr_ks_java_springboot_p_l001.entity.QuestionType;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface questionService {

    questionResponse create(questionRequest req);

    PageResponseDTO<questionResponse> getAll(Pageable pageable);

    PageResponseDTO<questionResponse> searchWithPaging(String content, QuestionType type, Pageable pageable);

    questionResponse getById(UUID id);

    questionResponse update(UUID id, questionRequest req);

    void delete(UUID id);
}
