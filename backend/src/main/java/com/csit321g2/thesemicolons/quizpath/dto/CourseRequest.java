
package com.csit321g2.thesemicolons.quizpath.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CourseRequest(
        @NotNull Long teacherId,
        @NotBlank @Size(max = 30) String courseCode,
        @NotBlank @Size(max = 150) String courseName,
        String description
) {
}
