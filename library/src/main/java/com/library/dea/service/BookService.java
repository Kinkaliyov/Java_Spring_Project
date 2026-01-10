package com.library.dea.service;

import com.library.dea.entity.Book;
import com.library.dea.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // create method (POST)
    public Book add(Book book) {
        return bookRepository.save(book);
    }

    // show all books (GET)
    public List<Book> showAll() {
        return bookRepository.findAll();
    }

    // show book by id (GET)
    public Book showById(@PathVariable Integer id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("There is no such a Book with the following ID! " + id));
    }

    // delete book (DELETE)
    public void deleteBook(@PathVariable Integer id) {
        bookRepository.deleteById(id);
    }

}
