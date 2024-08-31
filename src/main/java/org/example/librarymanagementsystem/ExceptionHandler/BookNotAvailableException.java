package org.example.librarymanagementsystem.ExceptionHandler;

public class BookNotAvailableException extends RuntimeException {
    public BookNotAvailableException(String errorMessage) {
        super(errorMessage);
    }
}
