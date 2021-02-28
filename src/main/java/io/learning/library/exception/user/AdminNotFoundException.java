package io.learning.library.exception.user;

public class AdminNotFoundException extends RuntimeException implements UserNotFoundException {
    public AdminNotFoundException(Long id){
        super("Admin not found with id: " + id);
    }
}
