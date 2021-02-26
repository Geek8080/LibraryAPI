package io.learning.library.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("books")
    private List<Author> authors;

    private float edition;

    private int availableCopies;

    private int issuedCopies;

    public Book(String name, List<Author> authors, float edition, int availableCopies, int issuedCopies) {
        this.name = name;
        this.authors = authors;
        this.edition = edition;
        this.availableCopies = availableCopies;
        this.issuedCopies = issuedCopies;
    }
}
