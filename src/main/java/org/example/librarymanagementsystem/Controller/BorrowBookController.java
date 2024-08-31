package org.example.librarymanagementsystem.Controller;

import jakarta.validation.Valid;
import org.example.librarymanagementsystem.DAO.Books;
import org.example.librarymanagementsystem.DTO.BorrowedRecordDTO;
import org.example.librarymanagementsystem.DTO.UserDetailsDTO;
import org.example.librarymanagementsystem.ExceptionHandler.BookNotAvailableException;
import org.example.librarymanagementsystem.Repository.BookRepository;
import org.example.librarymanagementsystem.Repository.UserDetailsRepository;
import org.example.librarymanagementsystem.Service.BorrowRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/BorrowBook")
@Validated
public class BorrowBookController {

    private static final Logger logger = LoggerFactory.getLogger(BorrowBookController.class);

    @Autowired
    private BorrowRecordService borrowRecordService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @PostMapping
    public ResponseEntity<String> borrowBook(@Valid @RequestBody BorrowedRecordDTO borrowedRecordDTO) {
        try {
            Books book = bookRepository.findByIsbnNo(borrowedRecordDTO.getBookIsbnNo());
            UserDetailsDTO userDetailsDTO = borrowedRecordDTO.getUserDetails();

            // Check if the book is null or not
            if (book == null) {
                throw new BookNotAvailableException("Book with ISBN " + borrowedRecordDTO.getBookIsbnNo() + " not found.");
            }


            return ResponseEntity.status(HttpStatus.OK).body(borrowRecordService.borrowBook(book, userDetailsDTO, borrowedRecordDTO.getBorrowedDate()));

        } catch (BookNotAvailableException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while borrowing the book");
        }
    }
}
