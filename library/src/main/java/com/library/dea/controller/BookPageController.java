package com.library.dea.controller;

import com.library.dea.entity.Book;
import com.library.dea.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookPageController {
    private final BookService bookService;


    public BookPageController(BookService bookService) {
        this.bookService = bookService;
    }

    // table
    @GetMapping // mvc
    public String showBooks(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,Model model) {
        Page<Book> bookPage = bookService.findPaginated(page, size);
        model.addAttribute("books", bookService.showAll());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookPage.getTotalPages());
        model.addAttribute("totalItems", bookPage.getTotalElements());
        model.addAttribute("size", size);
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
    public String save(@Valid @ModelAttribute Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return book.getId() == null ? "library/new" : "library/edit";
        }
        bookService.add(book);
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("book", bookService.showById(id));
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