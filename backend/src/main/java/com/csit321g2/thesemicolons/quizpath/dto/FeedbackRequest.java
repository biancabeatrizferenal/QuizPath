
package com.csit321g2.thesemicolons.quizpath.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FeedbackRequest(
    @NotNull Long attemptId,
    @NotBlank String feedbackText,
    String studyTips
) {
}
