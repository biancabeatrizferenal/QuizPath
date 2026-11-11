
package com.csit321g2.thesemicolons.quizpath.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csit321g2.thesemicolons.quizpath.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
