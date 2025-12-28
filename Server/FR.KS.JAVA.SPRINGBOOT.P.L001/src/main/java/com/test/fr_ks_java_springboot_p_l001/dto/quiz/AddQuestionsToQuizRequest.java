package com.test.fr_ks_java_springboot_p_l001.dto.quiz;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddQuestionsToQuizRequest {
    @NotEmpty
    private List<Long> questionIds;
}
