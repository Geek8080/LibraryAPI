package io.learning.library.exception.entities.user;

public class StudentNotFoundException extends RuntimeException implements UserNotFoundException {
    public StudentNotFoundException(Long id) {
        super("Student not found with id: " + id);
    }
}
