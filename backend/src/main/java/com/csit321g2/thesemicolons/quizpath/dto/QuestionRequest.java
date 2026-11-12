
package com.csit321g2.thesemicolons.quizpath.dto;

import com.csit321g2.thesemicolons.quizpath.entity.QuestionType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuestionRequest(
        @NotNull Long courseId,
        @NotNull Long topicId,
        @NotBlank String questionText,
        @NotNull QuestionType questionType,
        @NotNull @Min(1) Integer points
) {
}
