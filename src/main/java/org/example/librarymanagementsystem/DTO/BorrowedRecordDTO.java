package org.example.librarymanagementsystem.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BorrowedRecordDTO {


    @NotNull(message = "Book ISBN cannot be null")
    private String bookIsbnNo;

    @Valid
    private ExistingUserDetailsDTO userDetails;

    @Valid
    private NewUserDetailsDTO newUserDetails;

    @NotNull(message = "Borrowed date cannot be null")
    private Date borrowedDate;

    private Date returnedDate; // can be null initially if the book is not returned yet

    private boolean isReturned;

    public BorrowedRecordDTO(String bookIsbnNo, NewUserDetailsDTO newUser, Date borrowedDate) {
        this.bookIsbnNo = bookIsbnNo;
        this.newUserDetails = newUser;
        this.borrowedDate = borrowedDate;
        this.isReturned = false;  // Defaults to false when creating a new record
        this.returnedDate = null; // Defaults to null initially
    }

    public BorrowedRecordDTO(String bookIsbnNo, ExistingUserDetailsDTO existingUser, Date borrowedDate) {
        this.bookIsbnNo = bookIsbnNo;
        this.userDetails = existingUser;
        this.borrowedDate = borrowedDate;
        this.isReturned = false;  // Defaults to false when creating a new record
        this.returnedDate = null; // Defaults to null initially
    }


}
