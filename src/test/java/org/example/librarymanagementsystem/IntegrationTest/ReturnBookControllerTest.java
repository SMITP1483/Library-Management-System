package org.example.librarymanagementsystem.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.librarymanagementsystem.DTO.ReturnedBookDTO;
import org.example.librarymanagementsystem.Repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ReturnBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void ReturnBookTest_ShouldReturnBorrowRecordNotFound() throws Exception {

        ReturnedBookDTO returnedBookDTO = new ReturnedBookDTO("978-3-16-148410-0");

        //Act and assert: perform 404 not found request
        mockMvc.perform(post("/api/ReturnBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(returnedBookDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Requested Book record not found"));
    }

    @Test
    public void ReturnBookTest_ShouldReturnOkRequest() throws Exception {
        ReturnedBookDTO returnedBookDTO = new ReturnedBookDTO("978-8-596-52068-7");

        //Act and assert: perform 200 ok request
        mockMvc.perform(post("/api/ReturnBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(returnedBookDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Book Returned"));

        //Act and assert: After returning the book, book is now available
        boolean response = bookRepository.existsByIsbnNoAndIsAvailableTrue("978-8-596-52068-7");
        assertTrue(response);
    }


}
