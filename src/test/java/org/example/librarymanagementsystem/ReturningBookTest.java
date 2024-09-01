package org.example.librarymanagementsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.*;
import org.example.librarymanagementsystem.Controller.BorrowBookController;
import org.example.librarymanagementsystem.Controller.ReturnBookController;
import org.example.librarymanagementsystem.DAO.Books;
import org.example.librarymanagementsystem.DAO.BorrowedRecord;
import org.example.librarymanagementsystem.DTO.ReturnedBookDTO;
import org.example.librarymanagementsystem.ExceptionHandler.BookNotFoundException;
import org.example.librarymanagementsystem.Repository.BookRepository;
import org.example.librarymanagementsystem.Repository.BorrowRecordRepository;
import org.example.librarymanagementsystem.Service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReturnBookController.class)
public class ReturningBookTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @MockBean
    private ReturnBookController returnBookController;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private BorrowRecordRepository recordRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;


    @Test
    public void ReturnBook_WithInvalidIsbnArgument() throws Exception {
        //Invalid isbnNO argument pass inside the returnedBookDTO constructor
        ReturnedBookDTO returnedBookDTO = new ReturnedBookDTO("98-99");

        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();

        Set<ConstraintViolation<ReturnedBookDTO>> setOfViolations = localValidatorFactoryBean.validate(returnedBookDTO);

        //Assert for invalid method argument;
        assertFalse(setOfViolations.isEmpty());

    }

    @Test
    public void ReturnBook_WithBorrowedRecordNotFound() throws Exception {

        ReturnedBookDTO returnedBookDTO = new ReturnedBookDTO("978-3-16-148410-0");

        when(returnBookController.bookReturn(returnedBookDTO)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Requested Book record not found"));

        //Act and assert: perform the request and check for the 404 status
        mockMvc.perform(post("/api/ReturnBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(returnedBookDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Requested Book record not found"));
    }


    @Test
    public void AfterBookReturned_BookIsAvailable() throws Exception {

        ReturnedBookDTO returnedBookDTO = new ReturnedBookDTO("98-99");

        //After returning the book is now available to user
        when(bookRepository.updateBookAvailableStatus(returnedBookDTO.getIsbnNo(),true)).thenReturn(1);

    }

    @Test
    public void returnBook_InternalServerError_ShouldReturnException() throws Exception {
        ReturnedBookDTO returnedBookDTO = new ReturnedBookDTO("978-3-16-148410-0");

        when(returnBookController.bookReturn(returnedBookDTO)).thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred"));

        //Assert and act: perform the request and check for the 500 status
        mockMvc.perform(post("/api/ReturnBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(returnedBookDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred"));
    }


    @Test
    public void UserWebRequestValidation() throws Exception {
           ReturnedBookDTO returnedBookDTO = new ReturnedBookDTO("978-3-16-148410-0");

        when(returnBookController.bookReturn(returnedBookDTO)).thenReturn(ResponseEntity.status(HttpStatus.OK).body("Book Returned"));

        //Assert and act: perform the request and check for the 200 status
        mockMvc.perform(post("/api/ReturnBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(returnedBookDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Book Returned"));
    }


}
