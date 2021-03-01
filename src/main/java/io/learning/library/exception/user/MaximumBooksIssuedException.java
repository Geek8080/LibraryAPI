package io.learning.library.exception.user;

public class MaximumBooksIssuedException extends RuntimeException{
    public MaximumBooksIssuedException(){
        super("You have got maximum number of books in your name.");
    }
}
