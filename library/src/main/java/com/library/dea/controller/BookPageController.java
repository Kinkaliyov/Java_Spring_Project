package com.library.dea.controller;

import com.library.dea.dto.BookDTO;
import com.library.dea.entity.Book;
import com.library.dea.mapper.BookMapper;
import com.library.dea.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookPageController {
    private final BookService bookService;


    public BookPageController(BookService bookService) {
        this.bookService = bookService;
    }

    // table
    @GetMapping // mvc
    public String showBooks(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
                            @RequestParam(required = false) String keyword,Model model) {
        Page<Book> bookPage;

        if (keyword != null) {
            bookPage = bookService.search(keyword, page, size);
            model.addAttribute("keyword", keyword);
        } else {
            bookPage = bookService.findPaginated(page, size);
            model.addAttribute("keyword", null);
        }

        List<BookDTO> bookDTOs = bookPage.getContent().stream()
                .map(BookMapper::toDTO)
                .toList();

        model.addAttribute("books", bookDTOs);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookPage.getTotalPages());
        model.addAttribute("totalItems", bookPage.getTotalElements());
        model.addAttribute("size", size);

        return "library/list";
    }

    // add form
    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("book", new BookDTO());
        return "library/new";
    }

    // save
    @PostMapping
    public String save(@Valid @ModelAttribute("book") BookDTO bookDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return bookDTO.getId() == null ? "library/new" : "library/edit";
        }
        bookService.saveDTO(bookDTO);
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Book book = bookService.showById(id);
        model.addAttribute("book", BookMapper.toDTO(book));
        return "library/edit";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String searchBooks(@RequestParam String keyword,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "5") int size,
                              Model model) {
        Page<Book> bookPage = bookService.search(keyword, page, size);

        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("size", size);

        return "library/list";
    }



}