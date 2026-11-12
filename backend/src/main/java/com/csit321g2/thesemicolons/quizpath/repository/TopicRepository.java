
package com.csit321g2.thesemicolons.quizpath.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csit321g2.thesemicolons.quizpath.entity.TopicEntity;

public interface TopicRepository extends JpaRepository<TopicEntity, Long> {
}
