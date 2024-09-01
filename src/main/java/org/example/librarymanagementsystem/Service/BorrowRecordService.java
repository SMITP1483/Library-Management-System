package org.example.librarymanagementsystem.Service;

import org.example.librarymanagementsystem.DAO.Books;
import org.example.librarymanagementsystem.DAO.BorrowedRecord;
import org.example.librarymanagementsystem.DAO.UserDetails;
import org.example.librarymanagementsystem.DTO.UserDetailsDTO;
import org.example.librarymanagementsystem.ExceptionHandler.BookNotAvailableException;
import org.example.librarymanagementsystem.ExceptionHandler.BookNotFoundException;
import org.example.librarymanagementsystem.Repository.BookRepository;
import org.example.librarymanagementsystem.Repository.BorrowRecordRepository;
import org.example.librarymanagementsystem.Repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BorrowRecordService {
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;


    public String borrowBook(Books book, UserDetailsDTO userDetailsDTO, Date borrowedDate) {
        if (!bookRepository.existsBooksByIsbnNo(book.getIsbnNo()))
            throw new BookNotFoundException("Book not found");

        if (!bookRepository.existsByIsbnNoAndIsAvailableTrue(book.getIsbnNo()))
            throw new BookNotAvailableException("Requested Book is currently Not Available");

        UserDetails userDetails;
        if (userDetailsRepository.existsById(userDetailsDTO.getId()))
            userDetails = userDetailsRepository.findById(userDetailsDTO.getId()).get();
        else {
            userDetails = new UserDetails(userDetailsDTO.getFirstName(), userDetailsDTO.getLastName(), userDetailsDTO.getEmail());
            userDetailsRepository.save(userDetails);
        }

        borrowRecordRepository.save(new BorrowedRecord(book, userDetails, borrowedDate));
        bookRepository.updateBookAvailableStatus(book.getIsbnNo(), false);
        return "Book Borrowed";
    }

}
