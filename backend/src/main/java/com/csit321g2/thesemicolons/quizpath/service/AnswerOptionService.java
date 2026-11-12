
package com.csit321g2.thesemicolons.quizpath.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.csit321g2.thesemicolons.quizpath.dto.AnswerOptionRequest;
import com.csit321g2.thesemicolons.quizpath.dto.AnswerOptionResponse;
import com.csit321g2.thesemicolons.quizpath.entity.AnswerOptionEntity;
import com.csit321g2.thesemicolons.quizpath.entity.QuestionEntity;
import com.csit321g2.thesemicolons.quizpath.repository.AnswerOptionRepository;
import com.csit321g2.thesemicolons.quizpath.repository.QuestionRepository;

@Service
public class AnswerOptionService {

    private final AnswerOptionRepository answerOptionRepository;
    private final QuestionRepository questionRepository;

    public AnswerOptionService(
            AnswerOptionRepository answerOptionRepository,
            QuestionRepository questionRepository
    ) {
        this.answerOptionRepository = answerOptionRepository;
        this.questionRepository = questionRepository;
    }

    @Transactional
    public AnswerOptionResponse createAnswerOption(AnswerOptionRequest request) {
        QuestionEntity question = findQuestion(request.questionId());

        AnswerOptionEntity answerOption = new AnswerOptionEntity(
                question,
                request.optionText(),
                request.isCorrect()
        );

        return toResponse(answerOptionRepository.save(answerOption));
    }

    @Transactional(readOnly = true)
    public List<AnswerOptionResponse> getAllAnswerOptions() {
        return answerOptionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AnswerOptionResponse getAnswerOptionById(Long id) {
        return toResponse(findAnswerOption(id));
    }

    @Transactional
    public AnswerOptionResponse updateAnswerOption(Long id, AnswerOptionRequest request) {
        AnswerOptionEntity answerOption = findAnswerOption(id);
        QuestionEntity question = findQuestion(request.questionId());

        answerOption.setQuestion(question);
        answerOption.setOptionText(request.optionText());
        answerOption.setIsCorrect(request.isCorrect());

        return toResponse(answerOptionRepository.save(answerOption));
    }

    @Transactional
    public void deleteAnswerOption(Long id) {
        AnswerOptionEntity answerOption = findAnswerOption(id);
        answerOptionRepository.delete(answerOption);
    }

    private AnswerOptionEntity findAnswerOption(Long id) {
        return answerOptionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Answer option not found"
                ));
    }

    private QuestionEntity findQuestion(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Question not found"
                ));
    }

    private AnswerOptionResponse toResponse(AnswerOptionEntity answerOption) {
        return new AnswerOptionResponse(
                answerOption.getOptionId(),
                answerOption.getQuestion().getQuestionId(),
                answerOption.getQuestion().getQuestionText(),
                answerOption.getOptionText(),
                answerOption.getIsCorrect()
        );
    }
}
