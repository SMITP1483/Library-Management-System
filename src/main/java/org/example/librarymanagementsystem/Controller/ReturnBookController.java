package org.example.librarymanagementsystem.Controller;

import jakarta.validation.Valid;
import org.example.librarymanagementsystem.DAO.BorrowedRecord;
import org.example.librarymanagementsystem.DTO.ReturnedBookDTO;
import org.example.librarymanagementsystem.Repository.BookRepository;
import org.example.librarymanagementsystem.Repository.BorrowRecordRepository;
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

import java.util.List;

@RestController
@RequestMapping("api/ReturnBook")
@Validated
public class ReturnBookController {

    private static final Logger logger = LoggerFactory.getLogger(ReturnBookController.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowRecordRepository recordRepository;

    @PostMapping
    public ResponseEntity<String> bookReturn(@Valid @RequestBody ReturnedBookDTO returnedBookDTO) {

        try {
            String isbnNo = returnedBookDTO.getIsbnNo();
            List<BorrowedRecord> borrowedRecords = recordRepository.findBorrowedRecord(isbnNo);

            if(borrowedRecords.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Requested Book record not found");

            bookRepository.updateBookAvailableStatus(returnedBookDTO.getIsbnNo(), true);
            recordRepository.updateBorrowedRecord(returnedBookDTO.getReturnedDate(),isbnNo);
            return ResponseEntity.status(HttpStatus.OK).body("Book Returned");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }

    }

}
