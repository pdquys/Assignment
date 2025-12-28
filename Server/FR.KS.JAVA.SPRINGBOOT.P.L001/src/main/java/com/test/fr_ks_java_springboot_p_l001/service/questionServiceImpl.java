package com.test.fr_ks_java_springboot_p_l001.service;

import com.test.fr_ks_java_springboot_p_l001.dto.PageResponseDTO;
import com.test.fr_ks_java_springboot_p_l001.dto.answer.answerRequest;
import com.test.fr_ks_java_springboot_p_l001.dto.question.questionRequest;
import com.test.fr_ks_java_springboot_p_l001.dto.question.questionResponse;
import com.test.fr_ks_java_springboot_p_l001.entity.Answer;
import com.test.fr_ks_java_springboot_p_l001.entity.Question;
import com.test.fr_ks_java_springboot_p_l001.entity.QuestionType;
import com.test.fr_ks_java_springboot_p_l001.exception.NotFoundException;
import com.test.fr_ks_java_springboot_p_l001.repository.questionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class questionServiceImpl implements questionService {

    private final questionRepository questionRepository;

    @Override
    @Transactional
    public questionResponse create(questionRequest req) {
        Question q = new Question();
        q.setContent(req.content());
        q.setType(req.type());
        q.setScore(req.score());

        for (answerRequest ar : req.answers()) {
            Answer a = new Answer();
            a.setContent(ar.content());
            a.setIsCorrect(ar.isCorrect());
            q.getAnswers().add(a);
        }
        return toRes(questionRepository.save(q));
    }

    @Override
    public PageResponseDTO<questionResponse> getAll(Pageable pageable) {
        Page<Question> page = questionRepository.findByActiveTrue(pageable);

        List<questionResponse> items = page.getContent()
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
    public PageResponseDTO<questionResponse> searchWithPaging(String content, QuestionType type, Pageable pageable) {
        return null;
    }

    @Override
    public questionResponse getById(UUID id) {
        Question q = questionRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Question not found: " + id));
        return toRes(q);
    }

    @Override
    public questionResponse update(UUID id, questionRequest req) {
        Question q = questionRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Question not found: " + id));

        q.setContent(req.content());
        q.setType(req.type());
        q.setScore(req.score());

        q.getAnswers().clear();
        for (answerRequest ar : req.answers()) {
            Answer a = new Answer();
            a.setContent(ar.content());
            a.setIsCorrect(ar.isCorrect());
            q.getAnswers().add(a);
        }

        return toRes(questionRepository.save(q));
    }

    @Override
    public void delete(UUID id) {
        Question q = questionRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Question not found: " + id));
        q.setActive(false);
    }

    private questionResponse toRes(Question q) {
        return new questionResponse(
                q.getId(),
                q.getContent(),
                q.getType(),
                q.getScore(),
                q.getCreatedAt(),
                q.getUpdatedAt()
        );
    }
}
