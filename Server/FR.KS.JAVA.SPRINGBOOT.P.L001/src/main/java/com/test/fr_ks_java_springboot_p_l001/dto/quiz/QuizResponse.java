package com.test.fr_ks_java_springboot_p_l001.dto.quiz;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Quiz basic information response (without questions)")
public record QuizResponse(

        @Schema(description = "Quiz ID")
        UUID id,

        @Schema(description = "Quiz title", example = "Java Programming Basics")
        String title,

        @Schema(description = "Quiz description", example = "Test your knowledge of Java fundamentals")
        String description,

        @Schema(description = "Quiz duration in minutes", example = "60")
        Integer durationMinutes,

        @Schema(description = "Whether the quiz is active", example = "true")
        Boolean active,

        @Schema(description = "Total number of questions in this quiz", example = "10")
        Integer totalQuestions,

        Instant createAt,
        Instant updateAt
) {}
