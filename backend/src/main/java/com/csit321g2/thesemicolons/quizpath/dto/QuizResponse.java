
package com.csit321g2.thesemicolons.quizpath.dto;

import java.time.LocalDateTime;

public record QuizResponse(
        Long quizId,
        Long courseId,
        String courseName,
        String title,
        Integer timeLimitMinutes,
        Integer maxAttempts,
        LocalDateTime createdAt
) {
}
