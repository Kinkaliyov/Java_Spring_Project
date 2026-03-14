package com.library.dea.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public String handleBookNotFound(BookNotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error/book_not_found";
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    public String handleAuthorNotFound(AuthorNotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error/author_not_found";
    }

    @ExceptionHandler(PasswordsDoNotMatchException.class)
    public String handlePasswordsDoNotMatch(PasswordsDoNotMatchException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error/passwords_dont_match";
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleUserAlreadyExists(UserAlreadyExistsException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error/user_exists";
    }
}