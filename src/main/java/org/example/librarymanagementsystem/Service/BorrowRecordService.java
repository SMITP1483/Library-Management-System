package org.example.librarymanagementsystem.Service;

import org.example.librarymanagementsystem.DAO.Books;
import org.example.librarymanagementsystem.DAO.BorrowedRecord;
import org.example.librarymanagementsystem.DAO.UserDetails;
import org.example.librarymanagementsystem.DTO.NewUserDetailsDTO;
import org.example.librarymanagementsystem.ExceptionHandler.BookNotAvailableException;
import org.example.librarymanagementsystem.ExceptionHandler.BookNotFoundException;
import org.example.librarymanagementsystem.ExceptionHandler.UserAlreadyExistsException;
import org.example.librarymanagementsystem.ExceptionHandler.UserRecordNotFoundException;
import org.example.librarymanagementsystem.Repository.BookRepository;
import org.example.librarymanagementsystem.Repository.BorrowRecordRepository;
import org.example.librarymanagementsystem.Repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class BorrowRecordService {
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Transactional
    public String borrowBook(String isbnNo, Long userId, Date borrowedDate) {
        /* borrowing the book with existing user */
        checkBookExistence(isbnNo);
        UserDetails userDetails = userDetailsRepository.findById(userId).orElseThrow(() -> new UserRecordNotFoundException("User record not found with this id " + userId));

        performBorrowOperation(bookRepository.findByIsbnNo(isbnNo), userDetails, borrowedDate);
        return "Book Borrowed";
    }


    @Transactional
    public String borrowBook(String isbnNo, NewUserDetailsDTO newUserDetailsDTO, Date borrowedDate) {
        /* borrowing the book with new user */
        checkBookExistence(isbnNo);
        Books book = bookRepository.findByIsbnNo(isbnNo);

        List<UserDetails> users = userDetailsRepository.findUserDetailsByEmail(newUserDetailsDTO.getEmail());
        if (!users.isEmpty())
            throw new UserAlreadyExistsException("User already exists with the email " + newUserDetailsDTO.getEmail());

        UserDetails newUser = new UserDetails(newUserDetailsDTO.getFirstName(), newUserDetailsDTO.getLastName(), newUserDetailsDTO.getEmail());
        userDetailsRepository.save(newUser);

        performBorrowOperation(book, newUser, borrowedDate);
        return "Book Borrowed";
    }

    private void checkBookExistence(String isbnNo) {
        if (!bookRepository.existsBooksByIsbnNo(isbnNo))
            throw new BookNotFoundException("Book with ISBN " + isbnNo + " is not found.");

        if (!bookRepository.existsByIsbnNoAndIsAvailableTrue(isbnNo))
            throw new BookNotAvailableException("Requested book with ISBN " + isbnNo + " is currently not available.");
    }

    private void performBorrowOperation(Books book, UserDetails userDetails, Date borrowedDate) {
        borrowRecordRepository.save(new BorrowedRecord(book, userDetails, borrowedDate));
        synchronized (this) {
            bookRepository.updateBookAvailableStatus(book.getIsbnNo(), false);
        }
    }

    public Integer BookBorrowCountByUserId(String isbnNo, Long userId) {
        Integer count = borrowRecordRepository.countBookBorrowRecordByUserId(isbnNo, userId);

        if (count == null)
            throw new UserRecordNotFoundException("Record not found");
        return count;
    }

}
