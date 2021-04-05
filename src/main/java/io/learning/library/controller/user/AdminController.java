package io.learning.library.controller.user;

import io.learning.library.assembler.user.AdminModelAssembler;
import io.learning.library.entities.user.Admin;
import io.learning.library.exception.user.AdminNotFoundException;
import io.learning.library.repository.user.AdminRepository;
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
public class AdminController {

    AdminRepository adminRepository;
    AdminModelAssembler adminModelAssembler;

    public AdminController(AdminRepository adminRepository, AdminModelAssembler adminModelAssembler) {
        this.adminRepository = adminRepository;
        this.adminModelAssembler = adminModelAssembler;
    }

    @GetMapping("/admins")
    public CollectionModel<EntityModel<Admin>> all(){
        List<EntityModel<Admin>> admins = adminRepository.findAll().stream()
                .map(adminModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(admins,
                linkTo(methodOn(AdminController.class).all()).withSelfRel()
        );
    }

    @PostMapping("/admins")
    public ResponseEntity<?> newAdmin(@RequestBody Admin admin){
        EntityModel<Admin> adminEntityModel = adminModelAssembler.toModel(adminRepository.save(admin));

        return ResponseEntity
                .created(adminEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(adminEntityModel);
    }

    @GetMapping("/admins/{id}")
    public EntityModel<Admin> one(@PathVariable Long id){
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new AdminNotFoundException(id));

        return adminModelAssembler.toModel(admin);
    }

    @PutMapping("/admins/{id}")
    public ResponseEntity<EntityModel> updateAdmin(@RequestBody Admin newAdmin, @PathVariable Long id){
        Admin admin = adminRepository.findById(id).map(adm -> {
            if(!newAdmin.getName().equals("")){
                adm.setName(newAdmin.getName());
            }
            if(!newAdmin.getPassword().equals("")){
                adm.setPassword(newAdmin.getPassword());
            }
            return adminRepository.save(adm);
        }).orElseGet(() -> {
            newAdmin.setId(id);
            return adminRepository.save(newAdmin);
        });

        EntityModel<Admin> adminEntityModel = adminModelAssembler.toModel(admin);

        return ResponseEntity
                .created(adminEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(adminEntityModel);
    }

    @DeleteMapping("/admins/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long id){
        adminRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
