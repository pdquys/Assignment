package com.test.fr_ks_java_springboot_p_l001.dto.question;

import com.test.fr_ks_java_springboot_p_l001.dto.answer.answerResponse;
import com.test.fr_ks_java_springboot_p_l001.entity.QuestionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(description = "Question details with answers and associated quizzes (read-only)")
public record questionResponse(
        @Schema(description = "Question ID")
        UUID id,

        @Schema(description = "Question content", example = "What is Java?")
        String content,

        @Schema(description = "Question type", example = "SINGLE_CHOICE")
        QuestionType type,

        @Schema(description = "Points awarded for correct answer", example = "10")
        Integer score,

        Instant createdAt,

        Instant updatedAt,

        @Schema(description = "List of quizzes using this question (read-only, managed via Quiz API)")
                List<QuizInfoDTO> quizzes,

        @Schema(description = "List of answer choices")
        List<answerResponse> answers
) {
        @Schema(description = "Quiz summary information")
        public record QuizInfoDTO(
                @Schema(description = "Quiz ID")
                UUID id,
                @Schema(description = "Quiz title", example = "Java Programming Basics")
                String title
        ) {}
}

