package io.learning.library.exception;

public class BookAlreadyIssuedException extends RuntimeException{
    public BookAlreadyIssuedException(){
        super("The book has already been issued to you and reissued with today's date.");
    }
}
