package com.library.dea.controller;

import com.library.dea.dto.BookDTO;
import com.library.dea.entity.Book;
import com.library.dea.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books") //end points
@Tag(name = "Books API", description = "CRUD operations for books") //CRUD
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/all")
    @Operation(summary = "Get All Books")
    public List<Book> getAllBooks() {
        return bookService.showAll();
    }

    @GetMapping
    public Page<Book> getBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return bookService.getBooks(pageable);
    }

    @GetMapping("/find/title/{title}")
    @Operation(summary = "Get All Books by Title")
    public List<Book> getAllBooksByTitle(@PathVariable String title) {
        return bookService.getAllByTitle(title);
    }

    @GetMapping("/find/author/{author}")
    @Operation(summary = "Get All Books By Author")
    public List<Book> getAllBooksByAuthor(@PathVariable String author) {
        return bookService.getAllByAuthor(author);
    }

    @GetMapping("/find/price/{minPrice}")
    @Operation(summary = "Get Books by price")
    public List<Book> getAllByMinPrice(@PathVariable Double minPrice) {
        return bookService.getAllByMinPrice(minPrice);
    }

    @GetMapping("/find/amount/{minAmount}")
    @Operation(summary = "Get Books by amount")
    public List<Book> getAllByMinAmount(@PathVariable Integer minAmount) {
        return bookService.getAllByMinAmount(minAmount);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Book By Id")
    public Book getBook(@PathVariable Integer id) {
        return bookService.showById(id);
    }

    @PostMapping("/add")
    @Operation(summary = "Add Book")
    public Book createBook(@RequestBody Book book) {
        return bookService.add(book);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update Book")
    public Book updateBook(@PathVariable Integer id, @RequestBody BookDTO bookDTO){
        return bookService.update(id, bookDTO);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete Book")
    public void deleteBookById(@PathVariable Integer id) {
        bookService.deleteBook(id);
    }
}