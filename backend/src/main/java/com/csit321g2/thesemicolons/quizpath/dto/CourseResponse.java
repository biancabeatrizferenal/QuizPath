
package com.csit321g2.thesemicolons.quizpath.dto;

public record CourseResponse(
        Long courseId,
        Long teacherId,
        String teacherName,
        String courseCode,
        String courseName,
        String description
) {
}
