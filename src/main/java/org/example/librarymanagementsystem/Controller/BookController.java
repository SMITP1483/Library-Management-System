package org.example.librarymanagementsystem.Controller;

import jakarta.validation.Valid;
import org.example.librarymanagementsystem.DAO.Books;
import org.example.librarymanagementsystem.DTO.BooksDTO;
import org.example.librarymanagementsystem.ExceptionHandler.BookAlreadyExistsException;
import org.example.librarymanagementsystem.ExceptionHandler.BookNotFoundException;
import org.example.librarymanagementsystem.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("api/Books")
@Validated
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<String> addBookDetails(@Valid @RequestBody BooksDTO booksDTO) {
        try {
            return ResponseEntity.ok(bookService.addBookDetails(new Books(booksDTO.getIsbnNo(), booksDTO.getTitle(), booksDTO.getAuthorName(), booksDTO.getPublicationYear(), true)));
        } catch (BookNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BookAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding a book");
        }
    }

    @GetMapping
    public ResponseEntity<List<Books>> getAllBooks() {
        try {
            return ResponseEntity.ok(bookService.getAllBooks());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }


}
