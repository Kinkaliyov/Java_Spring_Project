package com.library.dea.controller;

import com.library.dea.entity.Book;
import com.library.dea.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/books")
public class BookPageController {
    private final BookService bookService;


    public BookPageController(BookService bookService) {
        this.bookService = bookService;
    }

    // table
    @GetMapping // mvc
    public String showBooks(Model model) {
        model.addAttribute("books", bookService.showAll());
        return "library/list";
    }

    // add form
    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("book", new Book());
        return "library/new";
    }

    // save
    @PostMapping
    public String save(@ModelAttribute Book book) {
        bookService.add(book);
        return "redirect:/books";
    }

}