package com.library.dea.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthorDTO {
    private Long id;

    @NotBlank
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    public AuthorDTO() {

    }

    public AuthorDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}