
package com.csit321g2.thesemicolons.quizpath.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csit321g2.thesemicolons.quizpath.entity.QuizEntity;

public interface QuizRepository extends JpaRepository<QuizEntity, Long> {
}
