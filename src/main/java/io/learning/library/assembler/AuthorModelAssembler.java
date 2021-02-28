package io.learning.library.assembler;

import io.learning.library.controller.AuthorController;
import io.learning.library.entities.Author;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class AuthorModelAssembler implements RepresentationModelAssembler<Author, EntityModel<Author>> {

    @Override
    public EntityModel<Author> toModel(Author entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(AuthorController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(AuthorController.class).all()).withRel("authors"),
                linkTo(methodOn(AuthorController.class).oneBooks(entity.getId())).withRel("books")
        );
    }

}
