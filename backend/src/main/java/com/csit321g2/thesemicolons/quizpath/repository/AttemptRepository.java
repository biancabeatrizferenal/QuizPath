
package com.csit321g2.thesemicolons.quizpath.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.csit321g2.thesemicolons.quizpath.entity.AttemptEntity;

@Repository
public interface AttemptRepository extends JpaRepository<AttemptEntity, Long> {
}
