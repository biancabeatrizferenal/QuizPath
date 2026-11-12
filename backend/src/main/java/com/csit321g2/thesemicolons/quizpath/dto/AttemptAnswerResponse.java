
package com.csit321g2.thesemicolons.quizpath.dto;

public record AttemptAnswerResponse(
        Long attemptAnswerId,
        Long attemptId,
        Long questionId,
        String questionText,
        Long optionId,
        String optionText,
        Boolean isCorrect
) {
}
