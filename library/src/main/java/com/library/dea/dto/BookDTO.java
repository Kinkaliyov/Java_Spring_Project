package com.library.dea.dto;

import com.library.dea.validation.ValidPrice;
import jakarta.validation.constraints.*;

public class BookDTO {

    private Integer id;

    @NotBlank(message = "Title is required")
    @Size(min = 4, max = 100, message = "Title must be between 4 and 100 characters")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(min = 4, max = 40, message = "Author must be between 4 and 40 characters")
    private String author;

    @NotNull(message = "Price is required")
//    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
//    @Digits(integer = 10, fraction = 2, message = "Price must have 2 decimal places")
    @ValidPrice
    private Double price;

    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Amount must be greater than 0")
    @Max(value = 1000, message = "Amount must be less than 1000")
    private Integer amount;


    public BookDTO() {

    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
