
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

import com.csit321g2.thesemicolons.quizpath.dto.AttemptRequest;
import com.csit321g2.thesemicolons.quizpath.dto.AttemptResponse;
import com.csit321g2.thesemicolons.quizpath.service.AttemptService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/attempts")
public class AttemptController {

    private final AttemptService attemptService;

    public AttemptController(AttemptService attemptService) {
        this.attemptService = attemptService;
    }

    @PostMapping
    public ResponseEntity<AttemptResponse> createAttempt(
            @Valid @RequestBody AttemptRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(attemptService.createAttempt(request));
    }

    @GetMapping
    public ResponseEntity<List<AttemptResponse>> getAllAttempts() {
        return ResponseEntity.ok(attemptService.getAllAttempts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttemptResponse> getAttemptById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(attemptService.getAttemptById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttemptResponse> updateAttempt(
            @PathVariable Long id,
            @Valid @RequestBody AttemptRequest request
    ) {
        return ResponseEntity.ok(attemptService.updateAttempt(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttempt(@PathVariable Long id) {
        attemptService.deleteAttempt(id);
        return ResponseEntity.noContent().build();
    }
}
