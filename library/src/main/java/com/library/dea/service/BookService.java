package com.library.dea.service;

import com.library.dea.entity.Book;
import com.library.dea.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

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
    // show book by author
    public List<Book> getAllByAuthor(@PathVariable String author) {
        return bookRepository.findByAuthor(author);
    }

    // show book by price
    public List<Book> getAllByPrice(@PathVariable Double price) {
        return bookRepository.findByMinPrice(price);
    }

    // show book by title
    public List<Book> getAllByTitle(@PathVariable String title) {
        return bookRepository.findByTitle(title);
    }

    // show book by amount
    public List<Book> getAllByAmount(@PathVariable Integer amount) {
        return bookRepository.findByAmount(amount);
    }

    // update book (PUT)
    public Book update(@PathVariable Integer id, @RequestBody Book updatedBook) {
        return bookRepository.findById(id).map(existing -> {
            existing.setTitle(updatedBook.getTitle());
            existing.setAuthor(updatedBook.getAuthor());
            existing.setPrice(updatedBook.getPrice());
            existing.setAmount(updatedBook.getAmount());
            return bookRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("There is no such a book with following id: " + id));
    }

    // delete book (DELETE)
    public void deleteBook(@PathVariable Integer id) {
        bookRepository.deleteById(id);
    }

}
