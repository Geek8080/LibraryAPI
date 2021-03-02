package io.learning.library.assembler;

import io.learning.library.entities.BookIssueDetails;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public class BookIssueDetailsModelAssembler implements RepresentationModelAssembler<BookIssueDetails, EntityModel<BookIssueDetails>> {
    @Override
    public EntityModel<BookIssueDetails> toModel(BookIssueDetails entity) {
        return EntityModel.of(entity);
    }
}
