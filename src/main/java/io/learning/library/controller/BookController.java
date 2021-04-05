package io.learning.library.controller;

import io.learning.library.assembler.AuthorModelAssembler;
import io.learning.library.assembler.BookModelAssembler;
import io.learning.library.entities.Author;
import io.learning.library.entities.Book;
import io.learning.library.exception.BookNotFoundException;
import io.learning.library.repository.BookRepository;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api")
public class BookController {

    BookRepository bookRepository;
    BookModelAssembler bookModelAssembler;
    AuthorModelAssembler authorModelAssembler;

    public BookController(BookRepository bookRepository, BookModelAssembler bookModelAssembler, AuthorModelAssembler authorModelAssembler) {
        this.bookRepository = bookRepository;
        this.bookModelAssembler = bookModelAssembler;
        this.authorModelAssembler = authorModelAssembler;
    }

    @GetMapping("/books")
    public CollectionModel<EntityModel<Book>> all(){
        List<EntityModel<Book>> books = bookRepository.findAll().stream()
                .map(bookModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(books,
                linkTo(methodOn(BookController.class).all()).withSelfRel()
        );
    }

    @PostMapping("/books")
    ResponseEntity<?> newBook(@RequestBody Book book){
        EntityModel<Book> bookEntityModel = bookModelAssembler.toModel(bookRepository.save(book));

        return ResponseEntity
                .created(bookEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(bookEntityModel);
    }

    @GetMapping("/books/{id}")
    public EntityModel<Book> one(@PathVariable Long id){
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));

        return bookModelAssembler.toModel(book);
    }

    @PutMapping("/books/{id}")
    ResponseEntity<?> updateBook(@RequestBody Book newBook, @PathVariable Long id){
        Book book = bookRepository.findById(id).map(bk -> {
            if (!newBook.getName().equals("")){
                bk.setName(newBook.getName());
            }
            if (newBook.getEdition()>0.0f){
                bk.setEdition(newBook.getEdition());
            }
            if (newBook.getAvailableCopies()>0){
                bk.setAvailableCopies(newBook.getAvailableCopies());
            }
            if (newBook.getIssuedCopies()>0){
                bk.setIssuedCopies(newBook.getIssuedCopies());
            }
            if (newBook.getAuthors()!=null && !newBook.getAuthors().isEmpty()){
                bk.setAuthors(newBook.getAuthors());
            }
            return bk;
        }).orElseGet(() -> {
           newBook.setId(bookRepository.save(newBook).getId());
           return newBook;
        });

        EntityModel<Book> bookEntityModel = bookModelAssembler.toModel(book);

        return ResponseEntity
                .created(bookEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(bookEntityModel);


    }

    @DeleteMapping("/books/{id}")
    ResponseEntity<?> deleteBook(@PathVariable Long id){
        bookRepository.deleteById(id);

        return ResponseEntity
                .noContent().build();
    }

    @GetMapping("/books/{id}/authors")
    public CollectionModel<EntityModel<Author>> oneAuthors(@PathVariable Long id){
        List<EntityModel<Author>> authors = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id))
                .getAuthors().stream().map(authorModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(authors,
                linkTo(methodOn(BookController.class).oneAuthors(id)).withSelfRel(),
                linkTo(methodOn(BookController.class).one(id)).withRel("book"),
                linkTo(methodOn(BookController.class).all()).withRel("books"),
                linkTo(methodOn(AuthorController.class).all()).withRel("authors")
        );
    }
}
