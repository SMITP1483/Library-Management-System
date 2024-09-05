package org.example.librarymanagementsystem.DAO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Books {
    @Id
    private String isbnNo;

    private String title;
    private String authorName;
    private int publicationYear;
    private boolean isAvailable;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BorrowedRecord> borrowedRecordList;

    public Books(String isbnNo, String title, String authorName, int publicationYear) {
        this.isbnNo = isbnNo;
        this.title = title;
        this.authorName = authorName;
        this.publicationYear = publicationYear;
        this.isAvailable = true;
    }

}
