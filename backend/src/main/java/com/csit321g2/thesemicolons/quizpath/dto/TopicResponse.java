
package com.csit321g2.thesemicolons.quizpath.dto;

public record TopicResponse(
        Long topicId,
        Long courseId,
        String courseName,
        String topicName,
        String description
) {
}
