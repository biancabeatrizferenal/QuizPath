
package com.csit321g2.thesemicolons.quizpath.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record QuizRequest(
        @NotNull Long courseId,
        @NotBlank @Size(max = 150) String title,
        @NotNull @Min(1) Integer timeLimitMinutes,
        @NotNull @Min(1) Integer maxAttempts
) {
}
