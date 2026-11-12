
package com.csit321g2.thesemicolons.quizpath.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "attempt_answers")
public class AttemptAnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attempt_answer_id")
    private Long attemptAnswerId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attempt_id", nullable = false)
    private AttemptEntity attempt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionEntity question;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "option_id", nullable = false)
    private AnswerOptionEntity option;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect = false;

    public AttemptAnswerEntity() {
    }

    public Long getAttemptAnswerId() {
        return attemptAnswerId;
    }

    public void setAttemptAnswerId(Long attemptAnswerId) {
        this.attemptAnswerId = attemptAnswerId;
    }

    public AttemptEntity getAttempt() {
        return attempt;
    }

    public void setAttempt(AttemptEntity attempt) {
        this.attempt = attempt;
    }

    public QuestionEntity getQuestion() {
        return question;
    }

    public void setQuestion(QuestionEntity question) {
        this.question = question;
    }

    public AnswerOptionEntity getOption() {
        return option;
    }

    public void setOption(AnswerOptionEntity option) {
        this.option = option;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
}
