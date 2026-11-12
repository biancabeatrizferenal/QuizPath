
package com.csit321g2.thesemicolons.quizpath.dto;

import com.csit321g2.thesemicolons.quizpath.entity.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank @Size(max = 100) String fullName,
        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank String password,
        @NotNull UserRole role
) {
}
