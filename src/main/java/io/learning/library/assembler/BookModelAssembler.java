package io.learning.library.assembler;

import io.learning.library.entities.Book;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class BookModelAssembler implements RepresentationModelAssembler<Book, EntityModel<Book>> {
    @Override
    public EntityModel<Book> toModel(Book entity) {
        return null;
    }
}
