
package com.csit321g2.thesemicolons.quizpath.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.csit321g2.thesemicolons.quizpath.entity.AttemptAnswerEntity;

@Repository
public interface AttemptAnswerRepository extends JpaRepository<AttemptAnswerEntity, Long> {

    boolean existsByAttemptAttemptIdAndQuestionQuestionId(
            Long attemptId,
            Long questionId
    );
}
