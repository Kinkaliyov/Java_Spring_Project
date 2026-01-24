package com.library.dea.repository;

import com.library.dea.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    // findByTitle
    @Query(
            value = "SELECT * FROM books WHERE LOWER(title) LIKE LOWER(CONCAT('%', :title, '%'))",
            nativeQuery = true
    )
    List<Book> findByTitle(String title);
    // findByAuthor
    @Query(
            value = "SELECT * FROM books WHERE LOWER(author) LIKE LOWER(CONCAT('%', :author, '%'))",
            nativeQuery = true
    )
    List<Book> findByAuthor(String author);

    //findByMinPrice
    @Query(
            value = "SELECT * FROM books WHERE price >= :minPrice",
            nativeQuery = true
    )
    List<Book> findByMinPrice(Double minPrice);

    //findByAmount
    @Query(
            value = "SELECT * FROM books WHERE amount >= :minAmount",
            nativeQuery = true
    )
    List<Book> findByMinAmount(Integer minAmount);
}