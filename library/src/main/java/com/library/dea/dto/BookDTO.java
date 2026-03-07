package com.library.dea.dto;

import com.library.dea.entity.Author;
import com.library.dea.validation.ValidPrice;
import jakarta.validation.constraints.*;

public class BookDTO {

    private Integer id;

    @NotBlank(message = "Title обязательно!!")
    @Size(min = 2, max = 100, message = "Title must be 2-100 characters")
    private String title;

//    @NotBlank(message = "Author обязательно!!")
//    @Size(min = 4, max = 100, message = "Author must be 4-100 characters")
//    private String author;

    @NotNull(message = "Price обязательно!!")
//    @DecimalMin(value="0.01", message = "Price must be at least 0.01")
//    @Digits(integer = 10, fraction = 2, message = "Price format: up to 10 digits and 2 decimals") // 10.2
    @ValidPrice
    private Double price;

    @NotNull(message = "Amount обязательно!!")
    @Min(value=1, message = " Min amount must be at least 1")
    @Max(value=1000, message = "Max amount must be not more than 1000")
    private Integer amount;

    private Long authorId;

    private String authorName;

    public BookDTO() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

//    public String getAuthor() {
//        return author;
//    }
//
//    public void setAuthor(String author) {
//        this.author = author;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}