package org.example.librarymanagementsystem.Controller;

import jakarta.validation.Valid;
import org.example.librarymanagementsystem.DAO.Books;
import org.example.librarymanagementsystem.DTO.BooksDTO;
import org.example.librarymanagementsystem.ExceptionHandler.BookAlreadyExistsException;
import org.example.librarymanagementsystem.ExceptionHandler.BookNotFoundException;
import org.example.librarymanagementsystem.Repository.BookRepository;
import org.example.librarymanagementsystem.Service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("api/Books")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);
    private static final String errorMessage = "An error occurred while processing your request";

    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<String> addBookDetails(@Valid @RequestBody BooksDTO booksDTO) {
        try {
            return ResponseEntity.ok(bookService.addBookDetails(new Books(booksDTO.getIsbnNo(), booksDTO.getTitle(), booksDTO.getAuthorName(), booksDTO.getPublicationYear())));
        } catch (BookAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error(errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @GetMapping
    public ResponseEntity<List<Books>> getAllBooks() {
        try {
            List<Books> books = bookService.getAllAvailableBooks();

            if (books.isEmpty()) {
                // Return 404 Not Found with an empty body if no books are available
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            return ResponseEntity.ok(books);

        } catch (Exception e) {
            logger.error(errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }

    }

    @GetMapping("/ByTitle")
    public ResponseEntity<List<Books>> getBooksByTitle(@RequestParam String title) {
        try {
            System.out.println(title);
            return ResponseEntity.status(HttpStatus.OK).body(bookService.findBooksByTitleContaining(title));
        } catch (BookNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
        } catch (Exception e) {
            logger.error(errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
