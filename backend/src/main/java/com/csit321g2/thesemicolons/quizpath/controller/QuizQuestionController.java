
package com.csit321g2.thesemicolons.quizpath.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csit321g2.thesemicolons.quizpath.dto.QuizQuestionRequest;
import com.csit321g2.thesemicolons.quizpath.dto.QuizQuestionResponse;
import com.csit321g2.thesemicolons.quizpath.service.QuizQuestionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/quiz-questions")
public class QuizQuestionController {

    private final QuizQuestionService quizQuestionService;

    public QuizQuestionController(QuizQuestionService quizQuestionService) {
        this.quizQuestionService = quizQuestionService;
    }

    @PostMapping
    public ResponseEntity<QuizQuestionResponse> createQuizQuestion(
            @Valid @RequestBody QuizQuestionRequest request) {

        QuizQuestionResponse response =
                quizQuestionService.createQuizQuestion(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<QuizQuestionResponse>> getAllQuizQuestions() {
        return ResponseEntity.ok(quizQuestionService.getAllQuizQuestions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizQuestionResponse> getQuizQuestionById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                quizQuestionService.getQuizQuestionById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuizQuestionResponse> updateQuizQuestion(
            @PathVariable Long id,
            @Valid @RequestBody QuizQuestionRequest request) {

        return ResponseEntity.ok(
                quizQuestionService.updateQuizQuestion(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuizQuestion(@PathVariable Long id) {
        quizQuestionService.deleteQuizQuestion(id);
        return ResponseEntity.noContent().build();
    }
}
