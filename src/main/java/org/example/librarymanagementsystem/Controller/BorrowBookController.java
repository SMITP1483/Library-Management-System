package org.example.librarymanagementsystem.Controller;

import jakarta.validation.Valid;
import org.example.librarymanagementsystem.DTO.BorrowedRecordDTO;
import org.example.librarymanagementsystem.DTO.ExistingUserDetailsDTO;
import org.example.librarymanagementsystem.DTO.NewUserDetailsDTO;
import org.example.librarymanagementsystem.ExceptionHandler.BookNotAvailableException;
import org.example.librarymanagementsystem.ExceptionHandler.BookNotFoundException;
import org.example.librarymanagementsystem.ExceptionHandler.UserAlreadyExistsException;
import org.example.librarymanagementsystem.ExceptionHandler.UserRecordNotFoundException;
import org.example.librarymanagementsystem.Repository.BookRepository;
import org.example.librarymanagementsystem.Service.BorrowRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/BorrowBook")
@Validated
public class BorrowBookController {

    private static final Logger logger = LoggerFactory.getLogger(BorrowBookController.class);

    @Autowired
    private BorrowRecordService borrowRecordService;

    @Autowired
    private BookRepository bookRepository;


    @PostMapping
    public ResponseEntity<String> borrowBookWithUserDetails(@Valid @RequestBody BorrowedRecordDTO borrowedRecordDTO) {

        try {
            String responseMessage;
            if (borrowedRecordDTO.getUserDetails() != null) {
                ExistingUserDetailsDTO existingUser = borrowedRecordDTO.getUserDetails();
                responseMessage = borrowRecordService.borrowBook(borrowedRecordDTO.getBookIsbnNo(), existingUser.getUserId(), borrowedRecordDTO.getBorrowedDate());
            } else if (borrowedRecordDTO.getNewUserDetails() != null) {
                NewUserDetailsDTO newUser = borrowedRecordDTO.getNewUserDetails();
                responseMessage = borrowRecordService.borrowBook(borrowedRecordDTO.getBookIsbnNo(), newUser, borrowedRecordDTO.getBorrowedDate());
            } else {
                throw new IllegalArgumentException("Invalid user details provided");
            }

            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);

        } catch (UserRecordNotFoundException | BookNotAvailableException | BookNotFoundException e) {
            logger.info("Not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UserAlreadyExistsException e) {
            logger.warn("User already exists. {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Internal Server Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while borrowing the book. Please try again later.");
        }
    }

    @PostMapping("/UserRecord")
    public ResponseEntity<String> borrowUserRecord(@RequestParam String isbnNo, @RequestParam Long userId) {
        try {
            String message = borrowRecordService.BookBorrowCountByUserId(isbnNo, userId) + " times borrowed this book (ISBN No: " + isbnNo + ")\n";
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (UserRecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");
        }
    }
}
