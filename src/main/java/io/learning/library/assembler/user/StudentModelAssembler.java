package io.learning.library.assembler.user;

import io.learning.library.controller.user.StudentController;
import io.learning.library.entities.user.Student;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class StudentModelAssembler implements RepresentationModelAssembler<Student, EntityModel<Student>> {

    @Override
    public EntityModel<Student> toModel(Student entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(StudentController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(StudentController.class).all()).withRel("students"),
                linkTo(methodOn(StudentController.class).books(entity.getId())).withRel("issuedBooks")
        );
    }
}
