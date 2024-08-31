package org.example.librarymanagementsystem.ExceptionHandler;

public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
