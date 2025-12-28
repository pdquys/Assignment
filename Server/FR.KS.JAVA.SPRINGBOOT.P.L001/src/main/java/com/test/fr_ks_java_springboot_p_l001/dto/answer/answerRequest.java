package com.test.fr_ks_java_springboot_p_l001.dto.answer;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record answerRequest (
        UUID id,                 // optional (update), null if create
        @NotBlank String content,
        boolean isCorrect
){
}
