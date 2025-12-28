package com.test.fr_ks_java_springboot_p_l001.controller;

import com.test.fr_ks_java_springboot_p_l001.dto.ApiResponse;
import com.test.fr_ks_java_springboot_p_l001.dto.submissions.ExamSubmitRequest;
import com.test.fr_ks_java_springboot_p_l001.dto.submissions.ExamSubmitResponse;
import com.test.fr_ks_java_springboot_p_l001.service.examService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/exam")
@RequiredArgsConstructor
@Tag(name = "Exam")
public class ExamController {

    private final examService examService;

    @Operation(
            summary = "Submit exam answers",
            description = """
            Submit exam with answers for automatic scoring. 
            
            **Scoring Rules:**
            - SINGLE_CHOICE: Must select exactly 1 correct answer
            - MULTIPLE_CHOICE: Must select ALL correct answers (no partial credit)
            - Pass threshold: 50% of total score
            
            **Side Effects:**
            - Saves submission to quiz_submissions table
            - Returns detailed results with per-question breakdown
            """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Exam submitted successfully",
                    content = @Content(schema = @Schema(implementation = ExamSubmitResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "User or Quiz not found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validation error or Quiz is inactive",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<ExamSubmitResponse>> submit(
            @Valid @RequestBody ExamSubmitRequest req) {

        ExamSubmitResponse data = examService.submit(req);
        return ResponseEntity.ok(ApiResponse.success(data, "Exam submitted successfully"));
    }
}
