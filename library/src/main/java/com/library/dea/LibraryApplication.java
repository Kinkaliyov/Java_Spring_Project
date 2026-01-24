package com.library.dea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);

//		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class); // bean storage
//
//		Book book = context.getBean(Book.class);
//
//		book.setTitle("Java");
//		book.setAuthor("Games Gosling");
//
//		System.out.println(book.getTitle());
//		System.out.println(book.getAuthor());
    }

}