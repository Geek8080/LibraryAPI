package io.learning.library.exception;

public class BookNotFoundException extends RuntimeException implements EntityNotFoundException {
    public BookNotFoundException(Long id) {
        super("Book not found with id: " + id);
    }
}
