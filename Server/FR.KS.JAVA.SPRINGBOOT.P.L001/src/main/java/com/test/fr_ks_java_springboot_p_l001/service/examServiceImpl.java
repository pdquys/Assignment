package com.test.fr_ks_java_springboot_p_l001.service;

import com.test.fr_ks_java_springboot_p_l001.dto.submissions.ExamSubmitRequest;
import com.test.fr_ks_java_springboot_p_l001.dto.submissions.ExamSubmitResponse;
import com.test.fr_ks_java_springboot_p_l001.entity.*;
import com.test.fr_ks_java_springboot_p_l001.exception.NotFoundException;
import com.test.fr_ks_java_springboot_p_l001.repository.QuizRepository;
import com.test.fr_ks_java_springboot_p_l001.repository.QuizSubmissionRepository;
import com.test.fr_ks_java_springboot_p_l001.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class examServiceImpl implements  examService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuizSubmissionRepository submissionRepository;

    @Override
    public ExamSubmitResponse submit(ExamSubmitRequest req) {

        // ================= 1. Load User =================
        User user = userRepository.findById(req.userId())
                .orElseThrow(() -> new NotFoundException("User not found: " + req.userId()));

        // ================= 2. Load Quiz (FIX N+1) =================
        Quiz quiz = quizRepository.findWithQuestionsById(req.quizId())
                .orElseThrow(() -> new NotFoundException("Quiz not found: " + req.quizId()));

        List<Question> questions = quiz.getQuestions();
        int totalQuestions = questions.size();

        if (totalQuestions == 0) {
            throw new IllegalArgumentException("Quiz has no questions");
        }

        // ================= 3. Map user answers =================
        Map<UUID, List<UUID>> submittedAnswersMap = req.answers().stream()
                .collect(Collectors.toMap(
                        ExamSubmitRequest.QuestionAnswerDTO::questionId,
                        ExamSubmitRequest.QuestionAnswerDTO::answerIds
                ));

        double achievedScore = 0;
        double totalScore = 0;
        int correctCount = 0;
        int wrongCount = 0;

        List<ExamSubmitResponse.QuestionResultDTO> questionResults = new ArrayList<>();

        // ================= 4. Evaluate each question =================
        for (Question q : questions) {

            totalScore += q.getScore();

            List<UUID> submittedIds = submittedAnswersMap.getOrDefault(q.getId(), List.of());

            List<UUID> correctIds = q.getAnswers().stream()
                    .filter(Answer::getIsCorrect)
                    .map(Answer::getId)
                    .toList();

            boolean isCorrect;

            if (q.getType() == QuestionType.SINGLE_CHOICE) {
                // SINGLE: chọn đúng 1 đáp án và đúng đáp án
                isCorrect = submittedIds.size() == 1
                        && correctIds.contains(submittedIds.get(0));
            } else {
                // MULTIPLE: chọn CHÍNH XÁC tập đáp án (không thiếu, không thừa)
                isCorrect = new HashSet<>(submittedIds).equals(new HashSet<>(correctIds));
            }

            if (isCorrect) {
                achievedScore += q.getScore();
                correctCount++;
            } else {
                wrongCount++;
            }

            questionResults.add(new ExamSubmitResponse.QuestionResultDTO(
                    q.getId(),
                    q.getContent(),
                    q.getScore(),
                    isCorrect,
                    submittedIds,
                    correctIds
            ));
        }

        double percentage = (achievedScore / totalScore) * 100;
        boolean passed = percentage >= 50;

        // ================= 5. Save Submission =================
        QuizSubmission submission = new QuizSubmission();
        submission.setUser(user);
        submission.setQuiz(quiz);
        submission.setScore(achievedScore);
//        submission.setTotalScore(totalScore);
//        submission.setPassed(passed);
        submission.setSubmissionTime(LocalDateTime.now());

        submissionRepository.save(submission);

        // ================= 6. Build Response =================
        return new ExamSubmitResponse(
                submission.getId(),
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                quiz.getId(),
                quiz.getTitle(),
                totalQuestions,
                correctCount,
                wrongCount,
                totalScore,
                achievedScore,
                percentage,
                passed,
                submission.getSubmissionTime(),
                questionResults
        );
    }
}
