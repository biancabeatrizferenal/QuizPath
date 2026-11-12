
package com.csit321g2.thesemicolons.quizpath.dto;

import com.csit321g2.thesemicolons.quizpath.entity.QuestionType;

public record QuestionResponse(
        Long questionId,
        Long courseId,
        String courseName,
        Long topicId,
        String topicName,
        String questionText,
        QuestionType questionType,
        Integer points
) {
}
