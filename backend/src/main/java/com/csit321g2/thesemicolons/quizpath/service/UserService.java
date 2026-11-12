
package com.csit321g2.thesemicolons.quizpath.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.csit321g2.thesemicolons.quizpath.dto.UserRequest;
import com.csit321g2.thesemicolons.quizpath.dto.UserResponse;
import com.csit321g2.thesemicolons.quizpath.entity.UserEntity;
import com.csit321g2.thesemicolons.quizpath.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        UserEntity user = new UserEntity(
                request.fullName(),
                request.email(),
                passwordEncoder.encode(request.password()),
                request.role()
        );

        return toResponse(userRepository.save(user));
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse getUserById(Long id) {
        return toResponse(findUser(id));
    }

    public UserResponse updateUser(Long id, UserRequest request) {
        UserEntity user = findUser(id);

        if (!user.getEmail().equals(request.email())
                && userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(request.role());

        return toResponse(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        UserEntity user = findUser(id);
        userRepository.delete(user);
    }

    private UserEntity findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));
    }

    private UserResponse toResponse(UserEntity user) {
        return new UserResponse(
                user.getUserId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
