package io.learning.library.controller;

import io.learning.library.assembler.AuthorModelAssembler;
import io.learning.library.assembler.BookModelAssembler;
import io.learning.library.entities.Author;
import io.learning.library.entities.Book;
import io.learning.library.exception.AuthorNotFoundException;
import io.learning.library.repository.AuthorRepository;
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
public class AuthorController {

    AuthorRepository authorRepository;
    BookRepository bookRepository;
    AuthorModelAssembler authorModelAssembler;
    BookModelAssembler bookModelAssembler;


    public AuthorController(AuthorRepository authorRepository, AuthorModelAssembler authorModelAssembler, BookModelAssembler bookModelAssembler, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.authorModelAssembler = authorModelAssembler;
        this.bookRepository = bookRepository;
        this.bookModelAssembler = bookModelAssembler;
    }

    @GetMapping("/authors")
    public CollectionModel<EntityModel<Author>> all(){
        List<EntityModel<Author>> authors = authorRepository.findAll().stream()
                .map(authorModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(authors,
                linkTo(methodOn(AuthorController.class).all()).withSelfRel()
        );
    }

    @PostMapping
    ResponseEntity<?> addNew(@RequestBody Author author){
        EntityModel<Author> authorEntityModel = authorModelAssembler.toModel(authorRepository.save(author));

        return ResponseEntity
                .created(authorEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(authorEntityModel);
    }

    @GetMapping("/authors/{id}")
    public EntityModel<Author> one(@PathVariable Long id){
        Author author = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));

        return authorModelAssembler.toModel(author);
    }

    @PutMapping("/authors/{id}")
    ResponseEntity<?> updateAuthor(@RequestBody Author newAuthor, @PathVariable Long id){
        Author author = authorRepository.findById(id).map(ath -> {
            if (!newAuthor.getName().equals("")){
                ath.setName(newAuthor.getName());
            }
            if (!newAuthor.getBooks().isEmpty()){
                ath.setBooks(newAuthor.getBooks());
            }
            return authorRepository.save(ath);
        }).orElseGet(() -> {
            newAuthor.setId(authorRepository.save(newAuthor).getId());
            return newAuthor;
        });

        EntityModel<Author> authorEntityModel = authorModelAssembler.toModel(author);

        return ResponseEntity
                .created(authorEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(authorEntityModel);
    }

    @DeleteMapping("/authors/{id}")
    ResponseEntity<?> deleteAuthor(@PathVariable Long id){
        authorRepository.deleteById(id);

        return ResponseEntity
                .noContent().build();
    }

    @GetMapping("/authors/{id}/books")
    public CollectionModel<EntityModel<Book>> oneBooks(@PathVariable Long id){
        List<EntityModel<Book>> books = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id)).
                getBooks().stream().map(bookModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(books,
                linkTo(methodOn(AuthorController.class).oneBooks(id)).withSelfRel(),
                linkTo(methodOn(AuthorController.class).one(id)).withRel("author"),
                linkTo(methodOn(AuthorController.class).all()).withRel("authors"),
                linkTo(methodOn(BookController.class).all()).withRel("books")
        );
    }
}
