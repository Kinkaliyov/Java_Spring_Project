package com.library.dea.controller;

import com.library.dea.entity.Book;
import com.library.dea.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/all")
    public List<Book> getAllBooks() {
        return bookService.showAll();
    }

    @GetMapping("/find/title/{title}")
    public List<Book> getAllBooksByTitle(@PathVariable String title) {
        return bookService.getAllByTitle(title);
    }

    @GetMapping("/find/author/{author}")
    public List<Book> getAllBooksByAuthor(@PathVariable String author) {
        return bookService.getAllByAuthor(author);
    }

    @GetMapping("/find/minPrice/{minPrice}")
    public List<Book> getAllBooksByMinPrice(@PathVariable Double minPrice) {
        return bookService.getAllByPrice(minPrice);
    }



    @GetMapping("/{id}")
    public Book getBook(@PathVariable Integer id) {
        return bookService.showById(id);
    }

    @PostMapping("/add")
    public Book createBook(@RequestBody Book book) {
        return bookService.add(book);
    }

    @PutMapping("/update/{id}")
    public Book updateBook(@PathVariable Integer id, @RequestBody Book book){
        return bookService.update(id, book);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBookById(@PathVariable Integer id) {
        bookService.deleteBook(id);
    }
}
