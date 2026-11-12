
package com.csit321g2.thesemicolons.quizpath.dto;

public record AnswerOptionResponse(
        Long optionId,
        Long questionId,
        String questionText,
        String optionText,
        Boolean isCorrect
) {
}
