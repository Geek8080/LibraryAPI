package io.learning.library.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Author {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "authors")
    private List<Book> books;

    public Author(String name) {
        this.name = name;
        this.books = new ArrayList<>();
    }
}
