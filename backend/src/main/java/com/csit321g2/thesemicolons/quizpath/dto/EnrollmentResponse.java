
package com.csit321g2.thesemicolons.quizpath.dto;

import java.time.LocalDateTime;

public record EnrollmentResponse(
        Long enrollmentId,
        Long courseId,
        String courseName,
        Long studentId,
        String studentName,
        LocalDateTime enrolledAt
) {
}
