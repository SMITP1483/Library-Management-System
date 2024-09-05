package org.example.librarymanagementsystem.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.librarymanagementsystem.Controller.BorrowBookController;
import org.example.librarymanagementsystem.DTO.BorrowedRecordDTO;
import org.example.librarymanagementsystem.DTO.ExistingUserDetailsDTO;
import org.example.librarymanagementsystem.DTO.NewUserDetailsDTO;
import org.example.librarymanagementsystem.Service.BorrowRecordService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BorrowBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void BorrowBookTestWithInvalidArgumentsForNewUser_ShouldReturnBadRequest() throws Exception {

        String isbnNo = "978-8-596-52068-7";
        NewUserDetailsDTO user = new NewUserDetailsDTO("", "patel", "smitpatel@gmail.com");

        BorrowedRecordDTO record = new BorrowedRecordDTO(isbnNo, user, new Date(2024, Calendar.AUGUST, 30, 8, 24));

        //Act and assert: perform the request with invalid argument and get 404 bad request
        mockMvc.perform(post("/api/BorrowBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void BorrowBookTestWithInvalidArgumentsForExistingUser_ShouldReturnBadRequest() throws Exception {

        String isbnNo = "978-8-596-52068-7";
        ExistingUserDetailsDTO existingUser = new ExistingUserDetailsDTO();

        BorrowedRecordDTO record = new BorrowedRecordDTO(isbnNo, existingUser, new Date(2024, Calendar.AUGUST, 30, 8, 24));

        /* Act and assert: perform the request with invalid argument and get 404 bad request */
        mockMvc.perform(post("/api/BorrowBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void BorrowBookTestWithBookNotFound_ShouldReturnNotFound() throws Exception {
        String isbnNo = "978-8-596-58168-7";
        NewUserDetailsDTO user = new NewUserDetailsDTO("smit", "patel", "smitpatel@gmail.com");

        BorrowedRecordDTO record = new BorrowedRecordDTO(isbnNo, user, new Date(2024, Calendar.AUGUST, 30, 8, 24));

        //Act and assert: perform the request and get 404 bad request
        mockMvc.perform(post("/api/BorrowBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Book with ISBN " + isbnNo + " is not found."));
    }

    @Test
    void BorrowBookTestWithBookNotAvailable_ShouldReturnNotFound() throws Exception {
        String isbnNo = "0-123-45678-9";
        NewUserDetailsDTO user = new NewUserDetailsDTO("smit", "patel", "smitpate35l@gmail.com");

        BorrowedRecordDTO record = new BorrowedRecordDTO(isbnNo, user, new Date(2024, Calendar.AUGUST, 30, 8, 24));

        //Act and assert: perform the request and get 404 bad request
        mockMvc.perform(post("/api/BorrowBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Requested book with ISBN " + isbnNo + " is currently not available."));
    }

    @Test
    void BorrowBookTestWithUserNotFoundWithId_ShouldReturnNotFound() throws Exception {
        String isbnNo = "978-8-596-52068-7";
        ExistingUserDetailsDTO existingUser = new ExistingUserDetailsDTO(236L);

        BorrowedRecordDTO record = new BorrowedRecordDTO(isbnNo, existingUser, new Date(2024, Calendar.AUGUST, 30, 8, 24));

        /* Act and assert: perform the request and get 404 bad request */
        mockMvc.perform(post("/api/BorrowBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User record not found with this id " + existingUser.getUserId()));
    }

    @Test
    void BorrowBookTestWithUserAlreadyExists_ShouldReturnConflict() throws Exception {
        String isbnNo = "978-8-596-52068-7";

        /* User already exists with this email, should return conflict status */
        NewUserDetailsDTO newUser = new NewUserDetailsDTO("smith", "patel", "smitpatel@gmail.com");

        BorrowedRecordDTO record = new BorrowedRecordDTO(isbnNo, newUser, new Date(2024, Calendar.AUGUST, 30, 8, 24));

        /* Act and assert: perform the request and get 409 Conflict status */
        mockMvc.perform(post("/api/BorrowBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isConflict())
                .andExpect(content().string("User already exists with the email " + newUser.getEmail()));
    }


    @Test
    void BorrowBookTestWithValidRequest_ShouldReturnOk() throws Exception {
        String isbnNo = "978-8-596-52068-7";
        ExistingUserDetailsDTO existingUser = new ExistingUserDetailsDTO(2L);

        /* With valid argument, should return ok status*/
        BorrowedRecordDTO record = new BorrowedRecordDTO(isbnNo, existingUser, new Date(2024, Calendar.AUGUST, 30, 8, 24));

        /* Act and assert: perform the request and get 200 ok status */
        mockMvc.perform(post("/api/BorrowBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isOk())
                .andExpect(content().string("Book Borrowed"));
    }

}
