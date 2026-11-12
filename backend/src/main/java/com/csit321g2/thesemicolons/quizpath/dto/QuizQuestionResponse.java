
package com.csit321g2.thesemicolons.quizpath.dto;

public record QuizQuestionResponse(
    Long quizQuestionId,
    Long quizId,
    String quizTitle,
    Long questionId,
    String questionText
) {
}
