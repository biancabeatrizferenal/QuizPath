
package com.csit321g2.thesemicolons.quizpath.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.csit321g2.thesemicolons.quizpath.dto.AttemptAnswerRequest;
import com.csit321g2.thesemicolons.quizpath.dto.AttemptAnswerResponse;
import com.csit321g2.thesemicolons.quizpath.entity.AnswerOptionEntity;
import com.csit321g2.thesemicolons.quizpath.entity.AttemptAnswerEntity;
import com.csit321g2.thesemicolons.quizpath.entity.AttemptEntity;
import com.csit321g2.thesemicolons.quizpath.entity.QuestionEntity;
import com.csit321g2.thesemicolons.quizpath.repository.AnswerOptionRepository;
import com.csit321g2.thesemicolons.quizpath.repository.AttemptAnswerRepository;
import com.csit321g2.thesemicolons.quizpath.repository.AttemptRepository;
import com.csit321g2.thesemicolons.quizpath.repository.QuestionRepository;
import com.csit321g2.thesemicolons.quizpath.repository.QuizQuestionRepository;

@Service
@Transactional
public class AttemptAnswerService {

    private final AttemptAnswerRepository attemptAnswerRepository;
    private final AttemptRepository attemptRepository;
    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;
    private final QuizQuestionRepository quizQuestionRepository;

    public AttemptAnswerService(
            AttemptAnswerRepository attemptAnswerRepository,
            AttemptRepository attemptRepository,
            QuestionRepository questionRepository,
            AnswerOptionRepository answerOptionRepository,
            QuizQuestionRepository quizQuestionRepository
    ) {
        this.attemptAnswerRepository = attemptAnswerRepository;
        this.attemptRepository = attemptRepository;
        this.questionRepository = questionRepository;
        this.answerOptionRepository = answerOptionRepository;
        this.quizQuestionRepository = quizQuestionRepository;
    }

    public AttemptAnswerResponse createAttemptAnswer(AttemptAnswerRequest request) {
        AttemptEntity attempt = findAttempt(request.attemptId());
        QuestionEntity question = findQuestion(request.questionId());
        AnswerOptionEntity option = findOption(request.optionId());

        validateQuestionAndOption(attempt, question, option);

        if (attemptAnswerRepository.existsByAttemptAttemptIdAndQuestionQuestionId(
                request.attemptId(), request.questionId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This question already has an answer for the attempt"
            );
        }

        AttemptAnswerEntity answer = new AttemptAnswerEntity();
        answer.setAttempt(attempt);
        answer.setQuestion(question);
        answer.setOption(option);
        answer.setIsCorrect(option.getIsCorrect());

        return toResponse(attemptAnswerRepository.saveAndFlush(answer));
    }

    @Transactional(readOnly = true)
    public List<AttemptAnswerResponse> getAllAttemptAnswers() {
        return attemptAnswerRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AttemptAnswerResponse getAttemptAnswerById(Long id) {
        return toResponse(findAttemptAnswer(id));
    }

    public AttemptAnswerResponse updateAttemptAnswer(
            Long id,
            AttemptAnswerRequest request
    ) {
        AttemptAnswerEntity answer = findAttemptAnswer(id);
        AttemptEntity attempt = findAttempt(request.attemptId());
        QuestionEntity question = findQuestion(request.questionId());
        AnswerOptionEntity option = findOption(request.optionId());

        validateQuestionAndOption(attempt, question, option);

        boolean changedPair =
                !answer.getAttempt().getAttemptId().equals(request.attemptId())
                || !answer.getQuestion().getQuestionId().equals(request.questionId());

        if (changedPair && attemptAnswerRepository.existsByAttemptAttemptIdAndQuestionQuestionId(
                request.attemptId(), request.questionId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This question already has an answer for the attempt"
            );
        }

        answer.setAttempt(attempt);
        answer.setQuestion(question);
        answer.setOption(option);
        answer.setIsCorrect(option.getIsCorrect());

        return toResponse(attemptAnswerRepository.saveAndFlush(answer));
    }

    public void deleteAttemptAnswer(Long id) {
        AttemptAnswerEntity answer = findAttemptAnswer(id);
        attemptAnswerRepository.delete(answer);
    }

    private void validateQuestionAndOption(
            AttemptEntity attempt,
            QuestionEntity question,
            AnswerOptionEntity option
    ) {
        if (!option.getQuestion().getQuestionId().equals(question.getQuestionId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The selected option does not belong to this question"
            );
        }

        if (!quizQuestionRepository.existsByQuizQuizIdAndQuestionQuestionId(
                attempt.getQuiz().getQuizId(),
                question.getQuestionId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "This question is not assigned to the attempt's quiz"
            );
        }
    }

    private AttemptAnswerEntity findAttemptAnswer(Long id) {
        return attemptAnswerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Attempt answer not found"
                ));
    }

    private AttemptEntity findAttempt(Long id) {
        return attemptRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Attempt not found"
                ));
    }

    private QuestionEntity findQuestion(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Question not found"
                ));
    }

    private AnswerOptionEntity findOption(Long id) {
        return answerOptionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Answer option not found"
                ));
    }

    private AttemptAnswerResponse toResponse(AttemptAnswerEntity answer) {
        return new AttemptAnswerResponse(
                answer.getAttemptAnswerId(),
                answer.getAttempt().getAttemptId(),
                answer.getQuestion().getQuestionId(),
                answer.getQuestion().getQuestionText(),
                answer.getOption().getOptionId(),
                answer.getOption().getOptionText(),
                answer.getIsCorrect()
        );
    }
}
