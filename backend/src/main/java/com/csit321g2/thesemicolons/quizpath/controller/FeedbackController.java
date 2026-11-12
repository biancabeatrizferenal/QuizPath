
package com.csit321g2.thesemicolons.quizpath.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.csit321g2.thesemicolons.quizpath.dto.FeedbackRequest;
import com.csit321g2.thesemicolons.quizpath.dto.FeedbackResponse;
import com.csit321g2.thesemicolons.quizpath.service.FeedbackService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FeedbackResponse createFeedback(@Valid @RequestBody FeedbackRequest request) {
        return feedbackService.createFeedback(request);
    }

    @GetMapping
    public List<FeedbackResponse> getAllFeedback() {
        return feedbackService.getAllFeedback();
    }

    @GetMapping("/{id}")
    public FeedbackResponse getFeedbackById(@PathVariable Long id) {
        return feedbackService.getFeedbackById(id);
    }

    @PutMapping("/{id}")
    public FeedbackResponse updateFeedback(
            @PathVariable Long id,
            @Valid @RequestBody FeedbackRequest request) {
        return feedbackService.updateFeedback(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
    }
}
