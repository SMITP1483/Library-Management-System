package org.example.librarymanagementsystem;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.librarymanagementsystem.Controller.BorrowBookController;
import org.example.librarymanagementsystem.DAO.Books;
import org.example.librarymanagementsystem.DTO.BorrowedRecordDTO;
import org.example.librarymanagementsystem.DTO.UserDetailsDTO;
import org.example.librarymanagementsystem.ExceptionHandler.BookNotAvailableException;
import org.example.librarymanagementsystem.Repository.BookRepository;
import org.example.librarymanagementsystem.Repository.UserDetailsRepository;
import org.example.librarymanagementsystem.Service.BorrowRecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BorrowBookController.class)
public class BorrowBookTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private UserDetailsRepository userDetailsRepository;

    @MockBean
    BorrowRecordService borrowRecordService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void borrowBook_InvalidArgument_ShouldReturnBadRequest() throws Exception {
        //Invalid user details with empty firstName and lastName
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO("", " ", "john@gmail.com");

        // we can also check the validation for the book details argument and date also
        BorrowedRecordDTO borrowedRecordDTO = new BorrowedRecordDTO("978-0-596-52068-7", userDetailsDTO, new Date(2024, Calendar.SEPTEMBER, 31, 8, 24));

        Set<ConstraintViolation<BorrowedRecordDTO>> violations = validator.validate(borrowedRecordDTO);

        // assertFalse will return true
        assertFalse(violations.isEmpty(), violations.toString());
    }

    @Test
    public void borrowBook_BookNotFound_ShouldReturnBadRequest() throws Exception {
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO("Jack", "Dason", "jack@gmail.com");

        Books book = new Books();
        when(bookRepository.findByIsbnNo("978-0-596-68-7")).thenReturn(book);

        // Requested book is not found in the library
        when(borrowRecordService.borrowBook(book, userDetailsDTO, new Date(2024, Calendar.SEPTEMBER, 31, 8, 24))).thenThrow(new BookNotAvailableException("Book not found"));

    }

    @Test
    public void borrowBook_BookNotAvailable_ShouldReturnNotFound() throws Exception {
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO("Jack", "Dason", "jack@gmail.com");

        Books book = new Books();
        when(bookRepository.findByIsbnNo("978-0-596-52068-7")).thenReturn(book);

        // Requested book is borrowed by someone else, that's why it's currently not available
        when(borrowRecordService.borrowBook(book, userDetailsDTO, new Date(2024, Calendar.SEPTEMBER, 31, 8, 24))).thenThrow(new BookNotAvailableException("Requested Book is currently Not Available"));

    }

    @Test
    public void borrowBook_BookIsAvailable_ShouldReturnOk() throws Exception {
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO("Jack", "Dason", "jack@gmail.com");

        Books book = new Books();
        when(bookRepository.findByIsbnNo("0-123-45678-9")).thenReturn(book);

        // Requested book is available
        when(borrowRecordService.borrowBook(book, userDetailsDTO, new Date(2024, Calendar.SEPTEMBER, 31, 8, 24))).thenReturn("Book Borrowed");

    }

    @Test
    public void BookBorrowedByUser_ShouldReturnOk() throws Exception {
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO("Jack", "Dason", "jack@gmail.com");

        Date borrowedDate = new Date(2024, Calendar.SEPTEMBER, 31, 8, 24);

        Books book = new Books();
        book.setIsbnNo("978-0-596-52068-7");
        when(bookRepository.findByIsbnNo("978-0-596-52068-7")).thenReturn(book);

        // Mock the BorrowRecordService behavior to simulate a successful borrow
        when(borrowRecordService.borrowBook(book, userDetailsDTO, borrowedDate))
                .thenReturn("Book Borrowed");

        BorrowedRecordDTO borrowedRecordDTO = new BorrowedRecordDTO(book.getIsbnNo(), userDetailsDTO, borrowedDate);

        mockMvc.perform(post("/api/BorrowBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(borrowedRecordDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Book Borrowed"));
    }


}
