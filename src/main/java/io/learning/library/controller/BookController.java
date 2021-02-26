package io.learning.library.controller;

import io.learning.library.assembler.BookModelAssembler;
import io.learning.library.repository.BookRepository;

import io.learning.library.entities.Book;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

//@RestController
public class BookController {

    BookRepository bookRepository;
    BookModelAssembler bookModelAssembler;

    public BookController(BookRepository bookRepository, BookModelAssembler bookModelAssembler) {
        this.bookRepository = bookRepository;
        this.bookModelAssembler = bookModelAssembler;
    }

    //@GetMapping("/books")
    List<Book> all(){
//        List<EntityModel<Book>> books = bookRepository.findAll().stream()
//                .map(bookModelAssembler::toModel).collect(Collectors.toList());
//
//        return CollectionModel.of(books,
//                linkTo(methodOn(BookController.class).all()).withSelfRel());
        List<Book> bookList = bookRepository.findAll();

        bookList.forEach(book -> book.getAuthors().forEach(
                author -> author.setBooks(new ArrayList<>())
        ));

        return bookList;
    }

}
