package com.library.dea.service.impl;

import com.library.dea.dto.BookDTO;
import com.library.dea.entity.Author;
import com.library.dea.entity.Book;
import com.library.dea.exception.BookNotFoundException;
import com.library.dea.mapper.BookMapper;
import com.library.dea.repository.AuthorRepository;
import com.library.dea.repository.BookRepository;
import com.library.dea.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookServiceimpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookServiceimpl(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public Book add(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> showAll() {
        return bookRepository.findAll();
    }

    @Override
    public Page<Book> getBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public List<Book> getAllByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    @Override
    public List<Book> getAllByMinPrice(Double price) {
        return bookRepository.findByMinPrice(price);
    }

    @Override
    public List<Book> getAllByMinAmount(Integer minAmount) {
        return bookRepository.findByMinAmount(minAmount);
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public Book showById(Integer id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found! ID: " + id));
    }

    @Override
    public Author findAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found!"));
    }

    @Override
    public Book update(Integer id, BookDTO updatedBook) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No book with following id!"));

        existing.setTitle(updatedBook.getTitle());
        existing.setPrice(updatedBook.getPrice());
        existing.setAmount(updatedBook.getAmount());

        if (updatedBook.getAuthorId() != null) {
            Author author = authorRepository.findById(updatedBook.getAuthorId())
                    .orElseThrow(() -> new RuntimeException("Author not found!"));
            existing.setAuthor(author);
        }

        return bookRepository.save(existing);
    }

    @Override
    public void deleteBook(Integer id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Page<Book> findPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return bookRepository.findAll(pageable);
    }

    @Override
    public Page<Book> search(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return bookRepository.findByTitleContainingIgnoreCase(keyword, pageable);
    }

    @Override
    public void saveDto(BookDTO bookDTO) {
        Book entity = BookMapper.toEntity(bookDTO);
        Author author = authorRepository.findById(bookDTO.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author Not Found"));
        entity.setAuthor(author);
        bookRepository.save(entity);
    }

    @Override
    public List<Book> getAllByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }
}