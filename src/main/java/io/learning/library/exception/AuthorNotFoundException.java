package io.learning.library.exception;

public class AuthorNotFoundException extends RuntimeException implements EntityNotFoundException{
    public AuthorNotFoundException(Long id) {
        super("Author not found with id: " + id);
    }
}
