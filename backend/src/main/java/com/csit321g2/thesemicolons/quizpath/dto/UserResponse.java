
package com.csit321g2.thesemicolons.quizpath.dto;

import com.csit321g2.thesemicolons.quizpath.entity.UserRole;

public record UserResponse(
        Long userId,
        String fullName,
        String email,
        UserRole role
) {
}
