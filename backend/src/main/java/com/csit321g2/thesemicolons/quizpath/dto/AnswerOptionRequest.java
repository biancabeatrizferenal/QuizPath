
package com.csit321g2.thesemicolons.quizpath.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AnswerOptionRequest(
        @NotNull Long questionId,
        @NotBlank String optionText,
        @NotNull Boolean isCorrect
) {
}
