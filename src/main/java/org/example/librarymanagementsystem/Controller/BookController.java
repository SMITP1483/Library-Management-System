package org.example.librarymanagementsystem.Controller;

import jakarta.validation.Valid;
import org.example.librarymanagementsystem.DAO.Books;
import org.example.librarymanagementsystem.DTO.BooksDTO;
import org.example.librarymanagementsystem.ExceptionHandler.BookAlreadyExistsException;
import org.example.librarymanagementsystem.Service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<String> addBookDetails(@Valid @RequestBody BooksDTO booksDTO) {
        try {
            return ResponseEntity.ok(bookService.addBookDetails(new Books(booksDTO.getIsbnNo(), booksDTO.getTitle(), booksDTO.getAuthorName(), booksDTO.getPublicationYear(), true)));
        } catch (BookAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding a book");
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
            logger.error("\nAn error occurred while retrieving the books, {}\n",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }


}
