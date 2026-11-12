
package com.csit321g2.thesemicolons.quizpath.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.csit321g2.thesemicolons.quizpath.dto.QuizQuestionRequest;
import com.csit321g2.thesemicolons.quizpath.dto.QuizQuestionResponse;
import com.csit321g2.thesemicolons.quizpath.entity.QuestionEntity;
import com.csit321g2.thesemicolons.quizpath.entity.QuizEntity;
import com.csit321g2.thesemicolons.quizpath.entity.QuizQuestionEntity;
import com.csit321g2.thesemicolons.quizpath.repository.QuestionRepository;
import com.csit321g2.thesemicolons.quizpath.repository.QuizQuestionRepository;
import com.csit321g2.thesemicolons.quizpath.repository.QuizRepository;

@Service
@Transactional
public class QuizQuestionService {

    private final QuizQuestionRepository quizQuestionRepository;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;

    public QuizQuestionService(
            QuizQuestionRepository quizQuestionRepository,
            QuizRepository quizRepository,
            QuestionRepository questionRepository) {
        this.quizQuestionRepository = quizQuestionRepository;
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
    }

    public QuizQuestionResponse createQuizQuestion(QuizQuestionRequest request) {
        QuizEntity quiz = getQuiz(request.quizId());
        QuestionEntity question = getQuestion(request.questionId());

        validateSameCourse(quiz, question);
        validateUniquePair(quiz.getQuizId(), question.getQuestionId(), null);

        QuizQuestionEntity quizQuestion = new QuizQuestionEntity();
        quizQuestion.setQuiz(quiz);
        quizQuestion.setQuestion(question);

        return toResponse(quizQuestionRepository.save(quizQuestion));
    }

    @Transactional(readOnly = true)
    public List<QuizQuestionResponse> getAllQuizQuestions() {
        return quizQuestionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public QuizQuestionResponse getQuizQuestionById(Long id) {
        return toResponse(getQuizQuestion(id));
    }

    public QuizQuestionResponse updateQuizQuestion(
            Long id,
            QuizQuestionRequest request) {

        QuizQuestionEntity quizQuestion = getQuizQuestion(id);
        QuizEntity quiz = getQuiz(request.quizId());
        QuestionEntity question = getQuestion(request.questionId());

        validateSameCourse(quiz, question);
        validateUniquePair(quiz.getQuizId(), question.getQuestionId(), id);

        quizQuestion.setQuiz(quiz);
        quizQuestion.setQuestion(question);

        return toResponse(quizQuestionRepository.save(quizQuestion));
    }

    public void deleteQuizQuestion(Long id) {
        QuizQuestionEntity quizQuestion = getQuizQuestion(id);
        quizQuestionRepository.delete(quizQuestion);
    }

    private QuizQuestionEntity getQuizQuestion(Long id) {
        return quizQuestionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Quiz question not found"
                ));
    }

    private QuizEntity getQuiz(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Quiz not found"
                ));
    }

    private QuestionEntity getQuestion(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Question not found"
                ));
    }

    private void validateSameCourse(QuizEntity quiz, QuestionEntity question) {
        if (!quiz.getCourse().getCourseId()
                .equals(question.getCourse().getCourseId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Quiz and question must belong to the same course"
            );
        }
    }

    private void validateUniquePair(
            Long quizId,
            Long questionId,
            Long currentId) {

        boolean exists = quizQuestionRepository
                .existsByQuizQuizIdAndQuestionQuestionId(quizId, questionId);

        if (exists) {
            if (currentId != null) {
                QuizQuestionEntity current = getQuizQuestion(currentId);

                if (current.getQuiz().getQuizId().equals(quizId)
                        && current.getQuestion().getQuestionId().equals(questionId)) {
                    return;
                }
            }

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Question is already assigned to this quiz"
            );
        }
    }

    private QuizQuestionResponse toResponse(QuizQuestionEntity quizQuestion) {
        return new QuizQuestionResponse(
                quizQuestion.getQuizQuestionId(),
                quizQuestion.getQuiz().getQuizId(),
                quizQuestion.getQuiz().getTitle(),
                quizQuestion.getQuestion().getQuestionId(),
                quizQuestion.getQuestion().getQuestionText()
        );
    }
}
