package com.test.fr_ks_java_springboot_p_l001.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;
import java.util.List;

@Schema(description = "Paginated response wrapper with navigation info")
public record PageResponseDTO<T>(

        @Schema(description = "List of items in current page")
        List<T> content,

        @Schema(description = "Current page number (0-indexed)", example = "0")
        int pageNumber,

        @Schema(description = "Number of items per page", example = "10")
        int pageSize,

        @Schema(description = "Total number of items across all pages", example = "100")
        long totalElements,

        @Schema(description = "Total number of pages", example = "10")
        int totalPages
) {

    /**
     * Creates a PageResponseDTO from Spring's Page object.
     */
    public static <T> PageResponseDTO<T> from(Page<T> page) {
        return new PageResponseDTO<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}

