package io.learning.library.repository;

import io.learning.library.entities.Author;
import io.learning.library.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    void deleteBooksByAuthorsIsContaining(Author author);
}
