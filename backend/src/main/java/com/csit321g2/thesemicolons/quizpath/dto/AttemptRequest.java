
package com.csit321g2.thesemicolons.quizpath.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record AttemptRequest(
        @NotNull Long quizId,
        @NotNull Long studentId,
        @DecimalMin("0.00") BigDecimal score,
        LocalDateTime submittedAt
) {
}
