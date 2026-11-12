
package com.csit321g2.thesemicolons.quizpath.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csit321g2.thesemicolons.quizpath.entity.QuizQuestionEntity;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestionEntity, Long> {

    boolean existsByQuizQuizIdAndQuestionQuestionId(Long quizId, Long questionId);

}
