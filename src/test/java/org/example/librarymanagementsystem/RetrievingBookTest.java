package org.example.librarymanagementsystem;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.librarymanagementsystem.Controller.BookController;
import org.example.librarymanagementsystem.DAO.Books;
import org.example.librarymanagementsystem.Repository.BookRepository;
import org.example.librarymanagementsystem.Service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class RetrievingBookTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @MockBean
    private BookService bookService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

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

        Books book1 = new Books("0-123-45678-9", "Clean Code", "Robert C. Martin", 2008);
        Books book2 = new Books("978-0-596-52068-7", "Learning Python", "Mark Lutz", 2013);

        //Mock behaviour of the repository
        when(bookService.getAllAvailableBooks()).thenReturn(List.of(book1, book2));

        //Act and assert
        mockMvc.perform(get("/api/Books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].isbnNo").value(book1.getIsbnNo()))
                .andExpect(jsonPath("$[1].isbnNo").value(book2.getIsbnNo()));
    }

}
