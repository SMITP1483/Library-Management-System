package org.example.librarymanagementsystem.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
public class BorrowedRecordDTO {

    @NotNull(message = "Book ISBN cannot be null")
    private String bookIsbnNo;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Borrowed date cannot be null")
    private Date borrowedDate;

    private Date returnedDate; // can be null initially if the book is not returned yet

    private boolean isReturned;

    public BorrowedRecordDTO(String bookIsbnNo, Long userId, Date borrowedDate) {
        this.bookIsbnNo = bookIsbnNo;
        this.userId = userId;
        this.borrowedDate = borrowedDate;
        this.isReturned = false;  // Defaults to false when creating a new record
        this.returnedDate = null; // Defaults to null initially
    }
}
