package org.example.librarymanagementsystem;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.librarymanagementsystem.Controller.BookController;
import org.example.librarymanagementsystem.DAO.Books;
import org.example.librarymanagementsystem.DTO.BooksDTO;
import org.example.librarymanagementsystem.ExceptionHandler.BookAlreadyExistsException;
import org.example.librarymanagementsystem.Service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class AddingBookTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @MockBean
    BookService bookService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;


    @Test
    void testAddBookDetails_InvalidISBN_ShouldReturnBadRequest() throws Exception {
        //Using an Invalid ISBN No. Format to test validation failure
        BooksDTO booksDTO = new BooksDTO("0-12678-9", "KGF", "Harper Lee", 1960, true);

        // Validating the BooksDTO object
        Set<ConstraintViolation<BooksDTO>> violations = validator.validate(booksDTO);

        assertFalse(violations.isEmpty());

    }

    @Test
    void testAddBookDetails_InvalidRequest_ShouldReturnBadRequest() throws Exception {
        BooksDTO booksDTO = new BooksDTO("978-3-16-148410-0", "KGF", "Harper Lee", 1960, true);

        //when user tried to insert the book which is already exists
        when(bookService.addBookDetails(any(Books.class)))
                .thenThrow(new BookAlreadyExistsException("Book with ISBN " + booksDTO.getIsbnNo() + " already exists"));

        // Assert and act: Perform the request and check the 400 status
        mockMvc.perform(post("/api/Books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(booksDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Book with ISBN " + booksDTO.getIsbnNo() + " already exists"));
    }

    @Test
    public void testAddBookDetails_ValidRequest_ShouldReturnOk() throws Exception {
        BooksDTO booksDTO = new BooksDTO("978-3-16-148450-0", "KGF", "Harper Lee", 1960, true);

        when(bookService.addBookDetails(any(Books.class)))
                .thenReturn("Book added successfully");

        // Assert and act: Perform the request and check the 200 status
        mockMvc.perform(post("/api/Books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(booksDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Book added successfully"));
    }

    @Test
    public void testAddBookDetails_InternalServerError_ShouldReturnInternalServerError() throws Exception {

        BooksDTO booksDTO = new BooksDTO("978-3-16-148450-0", "KGF", "Harper Lee", 1960, true);

        when(bookService.addBookDetails(any(Books.class))).thenThrow(new RuntimeException("Internal Server Error"));

        // Assert and act: Perform the request and check the 500 status
        mockMvc.perform(post("/api/Books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(booksDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred while adding a book"));
    }


}
