package com.library.dea.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public class RegisterForm {
    @NotBlank(message = "Юзер не может быть пустым")
    @Size(min = 4, max = 50)
    @Pattern(
            regexp = "^[a-zA-Z0-9_]+$",
            message = "Username может содержать только буквы, цифры и '_'."
    )
    private String username;
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, message = "Пароль должен быть не короче 8 символов")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!_]).{8,}$",
            message = "Пароль слишком слабый! Нужны цифры, разные регистры и спецсимволы."
    )
    private String password;
    public String getUsername() {
        return username;
    }
    @NotBlank(message = "Требуется проверка пароля.")
    @Size(min = 8, message = "Пароль должен быть не короче 8 символов")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!_]).{8,}$",
            message = "Пароль слишком слабый! Нужны цифры, разные регистры и спецсимволы."
    )
    private String confrimPassword;



    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfrimPassword() {
        return confrimPassword;
    }

    public void setConfrimPassword(String confrimPassword) {
        this.confrimPassword = confrimPassword;
    }
}