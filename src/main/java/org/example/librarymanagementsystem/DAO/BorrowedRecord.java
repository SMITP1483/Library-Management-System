package org.example.librarymanagementsystem.DAO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BorrowedRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_isbnNo")
    private Books book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDetails user;

    private Date borrowedDate;
    private Date returnedDate;
    private boolean isReturned;

    public BorrowedRecord(Books book, UserDetails user, Date borrowedDate) {
        this.book = book;
        this.user = user;
        this.borrowedDate = borrowedDate;
        this.isReturned = false;
        this.returnedDate = null;
    }

    public void setReturnedDate(Date returnedDate) {
        this.returnedDate = returnedDate;
        this.isReturned = returnedDate != null; // set isReturned true if the return date is provided
    }

}
