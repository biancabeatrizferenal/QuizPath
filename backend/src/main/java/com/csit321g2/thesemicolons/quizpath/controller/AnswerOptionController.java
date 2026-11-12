
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

import com.csit321g2.thesemicolons.quizpath.dto.AnswerOptionRequest;
import com.csit321g2.thesemicolons.quizpath.dto.AnswerOptionResponse;
import com.csit321g2.thesemicolons.quizpath.service.AnswerOptionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/answer-options")
public class AnswerOptionController {

    private final AnswerOptionService answerOptionService;

    public AnswerOptionController(AnswerOptionService answerOptionService) {
        this.answerOptionService = answerOptionService;
    }

    @PostMapping
    public ResponseEntity<AnswerOptionResponse> createAnswerOption(
            @Valid @RequestBody AnswerOptionRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(answerOptionService.createAnswerOption(request));
    }

    @GetMapping
    public ResponseEntity<List<AnswerOptionResponse>> getAllAnswerOptions() {
        return ResponseEntity.ok(answerOptionService.getAllAnswerOptions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnswerOptionResponse> getAnswerOptionById(@PathVariable Long id) {
        return ResponseEntity.ok(answerOptionService.getAnswerOptionById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnswerOptionResponse> updateAnswerOption(
            @PathVariable Long id,
            @Valid @RequestBody AnswerOptionRequest request
    ) {
        return ResponseEntity.ok(answerOptionService.updateAnswerOption(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnswerOption(@PathVariable Long id) {
        answerOptionService.deleteAnswerOption(id);
        return ResponseEntity.noContent().build();
    }
}
