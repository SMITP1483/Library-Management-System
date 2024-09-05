package org.example.librarymanagementsystem.ExceptionHandler;

public class UserRecordNotFoundException extends RuntimeException{
    public UserRecordNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
