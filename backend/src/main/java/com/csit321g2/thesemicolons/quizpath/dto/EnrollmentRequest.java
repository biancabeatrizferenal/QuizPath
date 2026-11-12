
package com.csit321g2.thesemicolons.quizpath.dto;

import jakarta.validation.constraints.NotNull;

public record EnrollmentRequest(
        @NotNull Long courseId,
        @NotNull Long studentId
) {
}
