package io.learning.library.assembler.user;

import io.learning.library.controller.user.AdminController;
import io.learning.library.entities.user.Admin;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class AdminModelAssembler implements RepresentationModelAssembler<Admin, EntityModel<Admin>> {

    @Override
    public EntityModel<Admin> toModel(Admin entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(AdminController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(AdminController.class).all()).withRel("admins")
        );
    }
}
