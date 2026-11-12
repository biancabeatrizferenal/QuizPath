
package com.csit321g2.thesemicolons.quizpath.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AttemptResponse(
        Long attemptId,
        Long quizId,
        String quizTitle,
        Long studentId,
        String studentName,
        BigDecimal score,
        LocalDateTime startedAt,
        LocalDateTime submittedAt
) {
}
