
package com.csit321g2.thesemicolons.quizpath.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.csit321g2.thesemicolons.quizpath.dto.QuizRequest;
import com.csit321g2.thesemicolons.quizpath.dto.QuizResponse;
import com.csit321g2.thesemicolons.quizpath.entity.CourseEntity;
import com.csit321g2.thesemicolons.quizpath.entity.QuizEntity;
import com.csit321g2.thesemicolons.quizpath.repository.CourseRepository;
import com.csit321g2.thesemicolons.quizpath.repository.QuizRepository;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final CourseRepository courseRepository;

    public QuizService(QuizRepository quizRepository, CourseRepository courseRepository) {
        this.quizRepository = quizRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public QuizResponse createQuiz(QuizRequest request) {
        CourseEntity course = findCourse(request.courseId());

        QuizEntity quiz = new QuizEntity(
                course,
                request.title(),
                request.timeLimitMinutes(),
                request.maxAttempts()
        );

        return toResponse(quizRepository.save(quiz));
    }

    @Transactional(readOnly = true)
    public List<QuizResponse> getAllQuizzes() {
        return quizRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public QuizResponse getQuizById(Long id) {
        return toResponse(findQuiz(id));
    }

    @Transactional
    public QuizResponse updateQuiz(Long id, QuizRequest request) {
        QuizEntity quiz = findQuiz(id);
        CourseEntity course = findCourse(request.courseId());

        quiz.setCourse(course);
        quiz.setTitle(request.title());
        quiz.setTimeLimitMinutes(request.timeLimitMinutes());
        quiz.setMaxAttempts(request.maxAttempts());

        return toResponse(quizRepository.save(quiz));
    }

    @Transactional
    public void deleteQuiz(Long id) {
        QuizEntity quiz = findQuiz(id);
        quizRepository.delete(quiz);
    }

    private QuizEntity findQuiz(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Quiz not found"
                ));
    }

    private CourseEntity findCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Course not found"
                ));
    }

    private QuizResponse toResponse(QuizEntity quiz) {
        return new QuizResponse(
                quiz.getQuizId(),
                quiz.getCourse().getCourseId(),
                quiz.getCourse().getCourseName(),
                quiz.getTitle(),
                quiz.getTimeLimitMinutes(),
                quiz.getMaxAttempts(),
                quiz.getCreatedAt()
        );
    }
}
