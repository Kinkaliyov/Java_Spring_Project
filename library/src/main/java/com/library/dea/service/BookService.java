package com.library.dea.service;

import com.library.dea.dto.BookDTO;
import com.library.dea.entity.Author;
import com.library.dea.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
public interface BookService {
    Book add(Book book);
    List<Book> showAll();
    Page<Book> getBooks(Pageable pageable);
    List<Book> getAllByTitle(String title);
    List<Book> getAllByMinPrice(Double price);
    List<Book> getAllByMinAmount(Integer minAmount);
    List<Author> getAllAuthors();
    Book showById(Integer id);
    Author findAuthorById(Long id);
    Book update(Integer id, BookDTO updatedBook);
    void deleteBook(Integer id);
    Page<Book> findPaginated(int page, int size);
    Page<Book> search(String keyword, int page, int size);
    void saveDto(BookDTO bookDTO);
    List<Book> getAllByAuthor(String author);
}
