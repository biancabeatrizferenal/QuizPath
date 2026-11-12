
package com.csit321g2.thesemicolons.quizpath.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csit321g2.thesemicolons.quizpath.entity.AnswerOptionEntity;

public interface AnswerOptionRepository extends JpaRepository<AnswerOptionEntity, Long> {
}
