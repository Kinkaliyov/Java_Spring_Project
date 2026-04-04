package com.library.dea.controller;

import com.library.dea.dto.BookDTO;
import com.library.dea.entity.Book;
import com.library.dea.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Books API", description = "CRUD operations for books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @Operation(summary = "Get All Books")
    public Page<Book> getBooks(Pageable pageable) {
        return bookService.getBooks(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Book By ID")
    public Book getBookById(@PathVariable Integer id) {
        return bookService.showById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new book")
    public void createBook(@Valid @RequestBody BookDTO bookDTO) {
        bookService.saveDto(bookDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Book")
    public Book updateBook(
            @Valid
            @PathVariable Integer id,
            @RequestBody BookDTO bookDTO) {
        return bookService.update(id, bookDTO);
    }
}