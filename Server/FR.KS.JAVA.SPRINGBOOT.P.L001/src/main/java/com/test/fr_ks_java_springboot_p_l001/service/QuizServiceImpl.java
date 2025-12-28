package com.test.fr_ks_java_springboot_p_l001.service;


import com.test.fr_ks_java_springboot_p_l001.dto.PageResponseDTO;
import com.test.fr_ks_java_springboot_p_l001.dto.quiz.QuizRequest;
import com.test.fr_ks_java_springboot_p_l001.dto.quiz.QuizResponse;
import com.test.fr_ks_java_springboot_p_l001.entity.Question;
import com.test.fr_ks_java_springboot_p_l001.entity.Quiz;
import com.test.fr_ks_java_springboot_p_l001.exception.NotFoundException;
import com.test.fr_ks_java_springboot_p_l001.repository.QuizRepository;
import com.test.fr_ks_java_springboot_p_l001.repository.UserRepository;
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
public class QuizServiceImpl implements QuizService{

    private  final QuizRepository quizRepository;
    private final questionRepository questionRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public QuizResponse create(QuizRequest req) {
        Quiz q = new Quiz();
        q.setTitle(req.title());
        q.setDescription(req.description());
        q.setDurationMinutes(req.durationMinutes());
        q.setActive(true);

        return toRes(quizRepository.save(q));
    }

    @Override
    public PageResponseDTO<QuizResponse> getAll(Pageable pageable) {
        Page<Quiz> page = quizRepository.findAllByActiveTrue(pageable);

        List<QuizResponse> items = page.getContent()
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
    public QuizResponse getById(UUID id) {
        Quiz q = quizRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Quiz not found: " + id));
        return toRes(q);
    }

    @Override
    @Transactional
    public QuizResponse update(UUID id, QuizRequest req) {
        Quiz q = quizRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Quiz not found: " + id));

        q.setTitle(req.title());
        q.setDescription(req.description());
        q.setDurationMinutes(req.durationMinutes());

        return toRes(quizRepository.save(q));
    }

    @Override
    public void delete(UUID id) {
        Quiz q = quizRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Quiz not found: " + id));
        q.setActive(false);
    }

    @Override
    public void addQuestions(UUID quizId, List<UUID> questionIds) {
        Quiz quiz = quizRepository.findByIdAndActiveTrue(quizId)
                .orElseThrow(() -> new NotFoundException("Quiz not found"));

        List<Question> questions = questionRepository.findAllById(questionIds);

        if (questions.size() != questionIds.size()) {
            throw new IllegalArgumentException("Some questions not found");
        }

        for (Question q : questions) {
            q.setQuiz(quiz);
        }

        questionRepository.saveAll(questions);
    }

    private QuizResponse toRes(Quiz q) {
        return new QuizResponse(
                q.getId(),
                q.getTitle(),
                q.getDescription(),
                q.getDurationMinutes(),
                q.getActive(),
                q.getQuestions() != null ? q.getQuestions().size() : 0,
                q.getCreatedAt(),
                q.getUpdatedAt()
        );
    }
}
