package io.learning.library.exception;

public class BookNotAvailableException extends RuntimeException{
    public BookNotAvailableException(Long id){
        super("No more copies of the book available: " + id);
    }
}
