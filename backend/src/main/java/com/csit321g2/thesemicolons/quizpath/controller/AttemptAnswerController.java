
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

import com.csit321g2.thesemicolons.quizpath.dto.AttemptAnswerRequest;
import com.csit321g2.thesemicolons.quizpath.dto.AttemptAnswerResponse;
import com.csit321g2.thesemicolons.quizpath.service.AttemptAnswerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/attempt-answers")
public class AttemptAnswerController {

    private final AttemptAnswerService attemptAnswerService;

    public AttemptAnswerController(AttemptAnswerService attemptAnswerService) {
        this.attemptAnswerService = attemptAnswerService;
    }

    @PostMapping
    public ResponseEntity<AttemptAnswerResponse> createAttemptAnswer(
            @Valid @RequestBody AttemptAnswerRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(attemptAnswerService.createAttemptAnswer(request));
    }

    @GetMapping
    public ResponseEntity<List<AttemptAnswerResponse>> getAllAttemptAnswers() {
        return ResponseEntity.ok(attemptAnswerService.getAllAttemptAnswers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttemptAnswerResponse> getAttemptAnswerById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(attemptAnswerService.getAttemptAnswerById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttemptAnswerResponse> updateAttemptAnswer(
            @PathVariable Long id,
            @Valid @RequestBody AttemptAnswerRequest request
    ) {
        return ResponseEntity.ok(
                attemptAnswerService.updateAttemptAnswer(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttemptAnswer(@PathVariable Long id) {
        attemptAnswerService.deleteAttemptAnswer(id);
        return ResponseEntity.noContent().build();
    }
}
