package io.learning.library.entities.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Admin {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String password;

    public Admin(String name, String password){
        this.name = name;
        this.password = password;
    }
}
