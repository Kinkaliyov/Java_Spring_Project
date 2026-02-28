package com.library.dea.service;

import com.library.dea.dto.RegisterForm;
import com.library.dea.entity.User;
import com.library.dea.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterForm form) {
        if (userRepository.existsByUsername(form.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();

        user.setUsername(form.getUsername());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        if (!form.getPassword().equals(form.getConfrimPassword())) {
            throw new RuntimeException("Passwords do not match");
        }
        user.setRole("ROLE_USER");

        userRepository.save(user);
    }
}