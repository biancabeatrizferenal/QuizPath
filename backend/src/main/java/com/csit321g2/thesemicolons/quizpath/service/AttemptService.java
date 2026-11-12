
package com.csit321g2.thesemicolons.quizpath.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.csit321g2.thesemicolons.quizpath.dto.AttemptRequest;
import com.csit321g2.thesemicolons.quizpath.dto.AttemptResponse;
import com.csit321g2.thesemicolons.quizpath.entity.AttemptEntity;
import com.csit321g2.thesemicolons.quizpath.entity.QuizEntity;
import com.csit321g2.thesemicolons.quizpath.entity.UserEntity;
import com.csit321g2.thesemicolons.quizpath.entity.UserRole;
import com.csit321g2.thesemicolons.quizpath.repository.AttemptRepository;
import com.csit321g2.thesemicolons.quizpath.repository.QuizRepository;
import com.csit321g2.thesemicolons.quizpath.repository.UserRepository;

@Service
@Transactional
public class AttemptService {

    private final AttemptRepository attemptRepository;
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;

    public AttemptService(
            AttemptRepository attemptRepository,
            QuizRepository quizRepository,
            UserRepository userRepository
    ) {
        this.attemptRepository = attemptRepository;
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
    }

    public AttemptResponse createAttempt(AttemptRequest request) {
        QuizEntity quiz = findQuiz(request.quizId());
        UserEntity student = findStudent(request.studentId());

        validateStudentRole(student);
        validateScore(request.score());

        AttemptEntity attempt = new AttemptEntity();
        attempt.setQuiz(quiz);
        attempt.setStudent(student);
        attempt.setScore(request.score());
        attempt.setSubmittedAt(request.submittedAt());

        AttemptEntity saved = attemptRepository.saveAndFlush(attempt);
        validateSubmissionTime(saved.getStartedAt(), saved.getSubmittedAt());

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<AttemptResponse> getAllAttempts() {
        return attemptRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AttemptResponse getAttemptById(Long id) {
        return toResponse(findAttempt(id));
    }

    public AttemptResponse updateAttempt(Long id, AttemptRequest request) {
        AttemptEntity attempt = findAttempt(id);
        QuizEntity quiz = findQuiz(request.quizId());
        UserEntity student = findStudent(request.studentId());

        validateStudentRole(student);
        validateScore(request.score());
        validateSubmissionTime(attempt.getStartedAt(), request.submittedAt());

        attempt.setQuiz(quiz);
        attempt.setStudent(student);
        attempt.setScore(request.score());
        attempt.setSubmittedAt(request.submittedAt());

        return toResponse(attemptRepository.saveAndFlush(attempt));
    }

    public void deleteAttempt(Long id) {
        AttemptEntity attempt = findAttempt(id);
        attemptRepository.delete(attempt);
    }

    private AttemptEntity findAttempt(Long id) {
        return attemptRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Attempt not found"
                ));
    }

    private QuizEntity findQuiz(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Quiz not found"
                ));
    }

    private UserEntity findStudent(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Student not found"
                ));
    }

    private void validateStudentRole(UserEntity student) {
        if (student.getRole() != UserRole.STUDENT) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only users with the STUDENT role can take quizzes"
            );
        }
    }

    private void validateScore(BigDecimal score) {
        if (score != null && score.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Score cannot be negative"
            );
        }
    }

    private void validateSubmissionTime(
            LocalDateTime startedAt,
            LocalDateTime submittedAt
    ) {
        if (submittedAt != null && submittedAt.isBefore(startedAt)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Submission time cannot be earlier than start time"
            );
        }
    }

    private AttemptResponse toResponse(AttemptEntity attempt) {
        return new AttemptResponse(
                attempt.getAttemptId(),
                attempt.getQuiz().getQuizId(),
                attempt.getQuiz().getTitle(),
                attempt.getStudent().getUserId(),
                attempt.getStudent().getFullName(),
                attempt.getScore(),
                attempt.getStartedAt(),
                attempt.getSubmittedAt()
        );
    }
}
