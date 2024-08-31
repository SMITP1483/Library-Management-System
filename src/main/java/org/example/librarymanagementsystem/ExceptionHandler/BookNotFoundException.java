package org.example.librarymanagementsystem.ExceptionHandler;

public class BookNotFoundException extends RuntimeException{
    public BookNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
