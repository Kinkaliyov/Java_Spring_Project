package com.library.dea.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public String handleRuntime(RuntimeException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error";
    }
}
