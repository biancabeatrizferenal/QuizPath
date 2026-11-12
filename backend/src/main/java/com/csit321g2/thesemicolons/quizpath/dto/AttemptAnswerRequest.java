
package com.csit321g2.thesemicolons.quizpath.dto;

import jakarta.validation.constraints.NotNull;

public record AttemptAnswerRequest(
        @NotNull Long attemptId,
        @NotNull Long questionId,
        @NotNull Long optionId
) {
}
