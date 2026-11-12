
package com.csit321g2.thesemicolons.quizpath.dto;

import java.time.LocalDateTime;

public record FeedbackResponse(
    Long feedbackId,
    Long attemptId,
    String feedbackText,
    String studyTips,
    LocalDateTime createdAt
) {
}
