package com.test.fr_ks_java_springboot_p_l001.controller;

import com.test.fr_ks_java_springboot_p_l001.dto.ApiResponse;
import com.test.fr_ks_java_springboot_p_l001.dto.PageResponseDTO;
import com.test.fr_ks_java_springboot_p_l001.dto.quiz.QuizRequest;
import com.test.fr_ks_java_springboot_p_l001.dto.quiz.QuizResponse;
import com.test.fr_ks_java_springboot_p_l001.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/quizzes")
@RequiredArgsConstructor
@Tag(name = "Quizzes", description = "Quiz management APIs")
public class QuizController {

    private final QuizService quizService;


    @Operation(
            summary = "Create new quiz",
            description = "Create a quiz with title, description, duration, and active status. Questions are added separately via question endpoints."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Quiz created successfully",
                    content = @Content(schema = @Schema(implementation = QuizResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Access denied - Requires ADMIN role",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<QuizResponse>> create(
            @Valid @RequestBody QuizRequest req) {

        QuizResponse data = quizService.create(req);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(data, "Quiz created successfully"));
    }

    @Operation(
            summary = "Get all quizzes",
            description = "Retrieve paginated list of quizzes with basic information (without questions)"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Quizzes retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PageResponseDTO.class))
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponseDTO<QuizResponse>>> getAll(
            @ParameterObject Pageable pageable) {

        PageResponseDTO<QuizResponse> data = quizService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(data, "Quizzes retrieved successfully"));
    }

    @Operation(
            summary = "Get quiz by ID",
            description = "Retrieve basic quiz information without questions. Use /details endpoint to get questions."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Quiz found",
                    content = @Content(schema = @Schema(implementation = QuizResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Quiz not found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuizResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                ApiResponse.success(quizService.getById(id), "Quiz found"));
    }

    @Operation(
            summary = "Update quiz",
            description = "Update quiz metadata (title, description, duration, active status). Does not affect questions."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Quiz updated successfully",
                    content = @Content(schema = @Schema(implementation = QuizResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Quiz not found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Access denied - Requires ADMIN role",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<QuizResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody QuizRequest req) {

        return ResponseEntity.ok(
                ApiResponse.success(quizService.update(id, req), "Quiz updated"));
    }

    @Operation(
            summary = "Delete quiz",
            description = "Soft delete a quiz. Also removes all quiz-question relationships."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Quiz deleted successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Quiz not found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Access denied - Requires ADMIN role",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        quizService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Quiz deleted"));
    }

    @Operation(
            summary = "Add single question to quiz",
            description = "Add one question to a quiz. Question must already exist. Idempotent - won't add duplicates."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Question added successfully",
                    content = @Content(schema = @Schema(implementation = QuizResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Quiz or Question not found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Access denied - Requires ADMIN role",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PostMapping("/{id}/questions")
    public ResponseEntity<ApiResponse<Void>> addQuestions(
            @PathVariable UUID id,
            @RequestBody List<UUID> questionIds) {

        quizService.addQuestions(id, questionIds);
        return ResponseEntity.ok(ApiResponse.success(null, "Questions added to quiz"));
    }
}
