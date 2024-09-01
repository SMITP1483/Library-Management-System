package org.example.librarymanagementsystem;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.librarymanagementsystem.Controller.BookController;
import org.example.librarymanagementsystem.DAO.Books;
import org.example.librarymanagementsystem.Service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@WebMvcTest(BookController.class)
public class RetrievingBookTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @MockBean
    private BookService bookService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void GetAllAvailableBooks_NoBooks_ShouldReturnNotFound() throws Exception {

        when(bookService.getAllAvailableBooks()).thenReturn(List.of());

        // Act and assert: perform the request and check for the 404 request
        mockMvc.perform(get("/api/Books"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void GetAllAvailableBooks_Exception_ShouldReturnServerError() throws Exception {
        when(bookService.getAllAvailableBooks()).thenThrow(new RuntimeException());

        //Act and assert: perform the request and check for the 500 status
        mockMvc.perform(get("/api/Books"))
                .andExpect(status().isInternalServerError());

    }

    @Test
    public void GetAllAvailableBooks_ShouldReturnOk() throws Exception {

        Set<ConstraintViolation<List<Books>>> setOfViolations =validator.validate(bookService.getAllAvailableBooks());

        assertTrue(setOfViolations.isEmpty());
    }
}
