package com.library.dea.service;

import com.library.dea.dto.AuthorDTO;
import org.springframework.data.domain.Page;
import java.util.List;

public interface AuthorService {
    List<AuthorDTO> getAllAuthors();
    AuthorDTO getAuthorById(Long id);
    AuthorDTO createAuthor(AuthorDTO author);
    AuthorDTO updateAuthor(Long id, AuthorDTO author);
    Page<AuthorDTO> getAuthorsPage(int page, int size);
    void deleteAuthor(Long id);
    AuthorDTO findById(Long id);
}