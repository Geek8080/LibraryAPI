package io.learning.library.assembler;

import io.learning.library.controller.BookController;
import io.learning.library.entities.Book;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class BookModelAssembler implements RepresentationModelAssembler<Book, EntityModel<Book>> {
    @Override
    public EntityModel<Book> toModel(Book entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(BookController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(BookController.class).all()).withRel("books"),
                linkTo(methodOn(BookController.class).oneAuthors(entity.getId())).withRel("authors")
                );
    }
}
