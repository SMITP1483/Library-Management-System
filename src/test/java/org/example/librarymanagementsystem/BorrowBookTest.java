package org.example.librarymanagementsystem;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.librarymanagementsystem.Controller.BorrowBookController;
import org.example.librarymanagementsystem.DTO.BorrowedRecordDTO;
import org.example.librarymanagementsystem.DTO.UserDetailsDTO;
import org.example.librarymanagementsystem.ExceptionHandler.BookNotAvailableException;
import org.example.librarymanagementsystem.Repository.BookRepository;
import org.example.librarymanagementsystem.Repository.BorrowRecordRepository;
import org.example.librarymanagementsystem.Repository.UserDetailsRepository;
import org.example.librarymanagementsystem.Service.BorrowRecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private BorrowRecordService borrowRecordService;

    @MockBean
    private BorrowBookController borrowBookController;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowRecordRepository borrowRecordRepository;

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
    public void borrowBook_BookNotFound_ShouldReturnNotFound() throws Exception {
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO("Jack", "Dason", "jack@gmail.com");

        BorrowedRecordDTO borrowedRecordDTO = new BorrowedRecordDTO("978-0-596-52068-7", userDetailsDTO, new Date(2024, Calendar.SEPTEMBER, 31, 8, 24));

        when(borrowBookController.borrowBook(borrowedRecordDTO)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found"));

        //Assert and act: Perform the request and check the 404 status
        mockMvc.perform(post("/api/BorrowBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(borrowedRecordDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Book not found"));

    }

    @Test
    public void borrowBook_BookNotAvailable_ShouldReturnNotFound() throws Exception {
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO("Jack", "Dason", "jack@gmail.com");

        BorrowedRecordDTO borrowedRecordDTO = new BorrowedRecordDTO("978-0-596-52068-7", userDetailsDTO, new Date(2024, Calendar.SEPTEMBER, 31, 8, 24));

        when(bookRepository.existsByIsbnNoAndIsAvailableTrue("978-0-596-52068-7")).thenThrow(new BookNotAvailableException("Requested Book is currently Not Available"));

        when(borrowBookController.borrowBook(borrowedRecordDTO)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Requested Book is currently Not Available"));

        //Assert and act: Perform the request and check the 404 status
        mockMvc.perform(post("/api/BorrowBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(borrowedRecordDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Requested Book is currently Not Available"));

    }

    @Test
    public void borrowBook_BookIsAvailable_ShouldReturnOk() throws Exception {

        String isbnNo = "0-123-45678-9";
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO("Jack", "Dason", "jack@gmail.com");
        BorrowedRecordDTO borrowedRecordDTO = new BorrowedRecordDTO(
                isbnNo,
                userDetailsDTO,
                new Date(2024, Calendar.AUGUST, 30, 8, 24)
        );

        when(bookRepository.existsByIsbnNoAndIsAvailableTrue(isbnNo)).thenReturn(true);

        // Act & Assert: Perform the request and check for 200 OK status
        mockMvc.perform(post("/api/BorrowBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(borrowedRecordDTO)))
                .andExpect(status().isOk());

    }


    @Test
    public void BookBorrowedByUser_ValidArgument_ShouldReturnOk() throws Exception {

        UserDetailsDTO userDetailsDTO = new UserDetailsDTO("Jack", "Dason", "jack@gmail.com");

        Date borrowedDate = new Date(2024, Calendar.SEPTEMBER, 31, 8, 24);

        BorrowedRecordDTO borrowedRecordDTO = new BorrowedRecordDTO("978-0-596-52068-7", userDetailsDTO, borrowedDate);

        when(borrowBookController.borrowBook(borrowedRecordDTO)).thenReturn(ResponseEntity.status(HttpStatus.OK).body("Book Borrowed"));

        //Assert and act: Perform the request and check the 200 status
        mockMvc.perform(post("/api/BorrowBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(borrowedRecordDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Book Borrowed"));
    }


}
