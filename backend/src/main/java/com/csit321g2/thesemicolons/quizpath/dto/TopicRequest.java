
package com.csit321g2.thesemicolons.quizpath.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TopicRequest(
        @NotNull Long courseId,
        @NotBlank @Size(max = 150) String topicName,
        String description
) {
}
