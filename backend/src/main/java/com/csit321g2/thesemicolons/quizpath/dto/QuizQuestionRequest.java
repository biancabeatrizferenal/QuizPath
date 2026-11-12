
package com.csit321g2.thesemicolons.quizpath.dto;

import jakarta.validation.constraints.NotNull;

public record QuizQuestionRequest(
    @NotNull Long quizId,
    @NotNull Long questionId
) {
}
