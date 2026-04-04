package com.library.dea.service.impl;

import com.library.dea.dto.AuthorDTO;
import com.library.dea.entity.Author;
import com.library.dea.exception.AuthorAlreadyExistsException;
import com.library.dea.exception.AuthorNotFoundException;
import com.library.dea.repository.AuthorRepository;
import com.library.dea.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor // Сама создаст конструктор для финальных полей
public class AuthorServiceimpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public AuthorDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("No Author with ID: " + id));
        return mapToDto(author);
    }

    @Override
    public AuthorDTO findById(Long id) {
        // Просто вызываем метод выше, чтобы не дублировать код
        return getAuthorById(id);
    }

    @Override
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        if (authorRepository.existsByName(authorDTO.getName())) {
            throw new AuthorAlreadyExistsException("Author Already Exists!");
        }
        Author author = mapToEntity(authorDTO);
        return mapToDto(authorRepository.save(author));
    }

    @Override
    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("No Author with ID: " + id));

        author.setName(authorDTO.getName());
        return mapToDto(authorRepository.save(author));
    }

    @Override
    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }

    @Override
    public Page<AuthorDTO> getAuthorsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return authorRepository.findAll(pageable)
                .map(this::mapToDto); // Используем наш метод вместо modelMapper для однообразия
    }

    // Вспомогательные методы для конвертации
    private AuthorDTO mapToDto(Author author) {
        return new AuthorDTO(author.getId(), author.getName());
    }

    private Author mapToEntity(AuthorDTO authorDTO) {
        Author author = new Author();
        author.setName(authorDTO.getName());
        return author;
    }
}