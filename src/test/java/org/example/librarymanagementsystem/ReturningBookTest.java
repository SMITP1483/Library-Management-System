package org.example.librarymanagementsystem;

import jakarta.validation.*;
import org.example.librarymanagementsystem.Controller.BorrowBookController;
import org.example.librarymanagementsystem.Controller.ReturnBookController;
import org.example.librarymanagementsystem.DTO.ReturnedBookDTO;
import org.example.librarymanagementsystem.ExceptionHandler.BookNotFoundException;
import org.example.librarymanagementsystem.Repository.BookRepository;
import org.example.librarymanagementsystem.Repository.BorrowRecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@WebMvcTest(ReturnBookController.class)
public class ReturningBookTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @MockBean
    private ReturnBookController returnBookController;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private BorrowRecordRepository recordRepository;

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void ReturnBook_WithInvalidIsbnArgument() throws Exception {
        //Invalid isbnNO argument pass inside the returnedBookDTO constructor
        ReturnedBookDTO returnedBookDTO = new ReturnedBookDTO("98-99");

        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();

        Set<ConstraintViolation<ReturnedBookDTO>> setOfViolations = localValidatorFactoryBean.validate(returnedBookDTO);
        System.out.println(setOfViolations);
        ;

        // It will throw Constraint violation exception
        assertThrows(ConstraintViolationException.class, () -> {
            localValidatorFactoryBean.validate(returnedBookDTO).stream().findFirst().ifPresent(violation -> {
                throw new ConstraintViolationException("ISBN-No must be in valid format (ISBN-10 or ISBN-13)", null);
            });
        });

    }

    @Test
    public void ReturnBook_WithBorrowedRecordNotFound() throws Exception {
        ReturnedBookDTO returnedBookDTO = new ReturnedBookDTO("98-99");

        assertThrows(BookNotFoundException.class, () -> {
            validator.validate(returnedBookDTO).stream().findFirst().ifPresent(violation -> {
                throw new BookNotFoundException("Book not found");
            });
        });
    }

    @Test
    public void ReturnBook_WithValidBorrowedRecordFound() throws Exception {

        ReturnedBookDTO returnedBookDTO = new ReturnedBookDTO("0-201-63361-2");

        when(returnBookController.bookReturn(returnedBookDTO)).thenReturn(ResponseEntity.ok("Book Returned"));
    }

    @Test
    public void AfterBookReturned_BookIsAvailable() throws Exception {
        ReturnedBookDTO returnedBookDTO = new ReturnedBookDTO("98-99");

        when(returnBookController.bookReturn(returnedBookDTO)).thenReturn(ResponseEntity.ok("Book Returned"));

        when(bookRepository.existsByIsbnNoAndIsAvailableTrue(returnedBookDTO.getIsbnNo())).thenReturn(true);

    }


}
