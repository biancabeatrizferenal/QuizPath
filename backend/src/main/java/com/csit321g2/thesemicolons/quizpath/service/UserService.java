
package com.csit321g2.thesemicolons.quizpath.service;

import org.springframework.stereotype.Service;

import com.csit321g2.thesemicolons.quizpath.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
