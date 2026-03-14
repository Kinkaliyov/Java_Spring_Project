package com.library.dea.service.impl;

import com.library.dea.dto.RegisterForm;
import com.library.dea.entity.User;
import com.library.dea.repository.UserRepository;
import com.library.dea.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceimpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceimpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void register(RegisterForm form) {
        if (!form.getPassword().equals(form.getConfrimPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        if (userRepository.existsByUsername(form.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(form.getUsername());

        user.setPassword(passwordEncoder.encode(form.getPassword()));

        user.setRole("ROLE_USER");

        userRepository.save(user);
    }
}