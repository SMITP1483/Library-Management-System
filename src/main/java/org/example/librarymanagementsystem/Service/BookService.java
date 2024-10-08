package org.example.librarymanagementsystem.Service;

import org.example.librarymanagementsystem.DAO.Books;
import org.example.librarymanagementsystem.ExceptionHandler.BookAlreadyExistsException;
import org.example.librarymanagementsystem.ExceptionHandler.BookNotFoundException;
import org.example.librarymanagementsystem.Repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private BookRepository bookRepository;

    public String addBookDetails(Books book) {

        if (bookRepository.existsBooksByIsbnNo(book.getIsbnNo())) {
            throw new BookAlreadyExistsException("Book with ISBN " + book.getIsbnNo() + " is already exists");
        } else {
            logger.info("Adding book details for ISBN: {}", book.getIsbnNo());
            bookRepository.save(book);
            return "Book added successfully";
        }

    }

    public List<Books> getAllAvailableBooks() {
        try {
            logger.info("\n\nRetrieving all books\n\n");
            return bookRepository.findAllAvailableBooks();
        } catch (Exception e) {
            logger.error("An error occurred while retrieving the list of books", e);
            throw new RuntimeException("An error occurred while retrieving the list of books", e);
        }
    }

    public List<Books> findBooksByTitleContaining(String title) {

        List<Books> booksList = bookRepository.findAvailableBooksByTitle(title);
        if (booksList.isEmpty())
            throw new BookNotFoundException("Request Book is not found");
        return booksList;

    }

}
