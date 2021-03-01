package io.learning.library.exception;

public class NoSuchBookIssuedException extends Throwable {
    public NoSuchBookIssuedException(Long id) {
        super("Book with id: " + id + " has not been issued to you.");
    }
}
