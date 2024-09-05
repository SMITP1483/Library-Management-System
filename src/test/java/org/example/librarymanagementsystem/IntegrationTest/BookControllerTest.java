package org.example.librarymanagementsystem.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.librarymanagementsystem.DTO.BooksDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional //Ensure that all changes are rolled back after the test
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void AddBookTest_WithInvalidArguments_ShouldReturnBadRequest() throws Exception {
        /* passing the invalid argument */
        BooksDTO book = new BooksDTO("0-123-45678-9", "", "Harper Lee", 1960, true);

        //Act and assert: should return 400 bad request
        mockMvc.perform(post("/api/Books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void AddBookTest_ShouldReturnBookAlreadyExists() throws Exception {

        BooksDTO book = new BooksDTO("0-123-45678-9", "KGF", "Harper Lee", 1960, true);


        //Act and assert: should return 400 bad request (due to duplicate record)
        mockMvc.perform(post("/api/Books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Book with ISBN " + book.getIsbnNo() + " is already exists"));
    }

    @Test
    void AddBookTest_ShouldReturnOk() throws Exception {
        BooksDTO book = new BooksDTO("0-123-45978-9", "Positive thinking", "Swami vivekanand", 1910, true);

        /* Act and assert: should return 200 ok request */
        mockMvc.perform(post("/api/Books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(content().string("Book added successfully"));
    }

    @Test
    void RetrieveAvailableBookTest_ShouldReturnOk() throws Exception {

        mockMvc.perform(get("/api/Books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("["
                        + "{"
                        + "\"isbnNo\": \"0-123-45678-9\","
                        + "\"title\": \"Clean Code\","
                        + "\"authorName\": \"Robert C. Martin\","
                        + "\"publicationYear\": 2008,"
                        + "\"available\": true"
                        + "},"
                        + "{"
                        + "\"isbnNo\": \"\","
                        + "\"title\": \"The Pragmatic Programmer\","
                        + "\"authorName\": \"Andrew Hunt\","
                        + "\"publicationYear\": 1999,"
                        + "\"available\": true"
                        + "},"
                        + "{"
                        + "\"isbnNo\": \"978-8-596-52068-7\","
                        + "\"title\": \"The Pragmatic Programmer\","
                        + "\"authorName\": \"Andrew Hunt\","
                        + "\"publicationYear\": 1999,"
                        + "\"available\": true"
                        + "}"
                        + "]"));
    }

    @Test
    void RetrieveAvailableBookByTitleTest_ShouldReturnOk() throws Exception {

        mockMvc.perform(get("/api/Books/ByTitle?title=programmer"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("["
                        + "{"
                        + "\"isbnNo\": \"\","
                        + "\"title\": \"The Pragmatic Programmer\","
                        + "\"authorName\": \"Andrew Hunt\","
                        + "\"publicationYear\": 1999,"
                        + "\"available\": true"
                        + "},"
                        + "{"
                        + "\"isbnNo\": \"978-8-596-52068-7\","
                        + "\"title\": \"The Pragmatic Programmer\","
                        + "\"authorName\": \"Andrew Hunt\","
                        + "\"publicationYear\": 1999,"
                        + "\"available\": true"
                        + "}"
                        + "]"));
    }


}
