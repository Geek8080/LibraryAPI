package io.learning.library.repository;

import io.learning.library.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author readAuthorByNameEqualsIgnoringCase(String author);
}
