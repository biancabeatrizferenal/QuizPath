
package com.csit321g2.thesemicolons.quizpath.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.csit321g2.thesemicolons.quizpath.dto.FeedbackRequest;
import com.csit321g2.thesemicolons.quizpath.dto.FeedbackResponse;
import com.csit321g2.thesemicolons.quizpath.entity.AttemptEntity;
import com.csit321g2.thesemicolons.quizpath.entity.FeedbackEntity;
import com.csit321g2.thesemicolons.quizpath.repository.AttemptRepository;
import com.csit321g2.thesemicolons.quizpath.repository.FeedbackRepository;

@Service
@Transactional
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final AttemptRepository attemptRepository;

    public FeedbackService(
            FeedbackRepository feedbackRepository,
            AttemptRepository attemptRepository) {
        this.feedbackRepository = feedbackRepository;
        this.attemptRepository = attemptRepository;
    }

    public FeedbackResponse createFeedback(FeedbackRequest request) {
        AttemptEntity attempt = findAttempt(request.attemptId());

        FeedbackEntity feedback = new FeedbackEntity();
        feedback.setAttempt(attempt);
        feedback.setFeedbackText(request.feedbackText());
        feedback.setStudyTips(request.studyTips());

        return toResponse(feedbackRepository.saveAndFlush(feedback));
    }

    @Transactional(readOnly = true)
    public List<FeedbackResponse> getAllFeedback() {
        return feedbackRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public FeedbackResponse getFeedbackById(Long id) {
        return toResponse(findFeedback(id));
    }

    public FeedbackResponse updateFeedback(Long id, FeedbackRequest request) {
        FeedbackEntity feedback = findFeedback(id);
        AttemptEntity attempt = findAttempt(request.attemptId());

        feedback.setAttempt(attempt);
        feedback.setFeedbackText(request.feedbackText());
        feedback.setStudyTips(request.studyTips());

        return toResponse(feedbackRepository.saveAndFlush(feedback));
    }

    public void deleteFeedback(Long id) {
        FeedbackEntity feedback = findFeedback(id);
        feedbackRepository.delete(feedback);
    }

    private FeedbackEntity findFeedback(Long id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Feedback not found"
                ));
    }

    private AttemptEntity findAttempt(Long id) {
        return attemptRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Attempt not found"
                ));
    }

    private FeedbackResponse toResponse(FeedbackEntity feedback) {
        return new FeedbackResponse(
                feedback.getFeedbackId(),
                feedback.getAttempt().getAttemptId(),
                feedback.getFeedbackText(),
                feedback.getStudyTips(),
                feedback.getCreatedAt()
        );
    }
}
