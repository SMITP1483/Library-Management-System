package org.example.librarymanagementsystem.DAO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserDetails_Id_Generator")
    @SequenceGenerator(name = "UserDetails_Id_Generator", allocationSize = 1)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;

    @OneToMany(mappedBy = "user")
    private List<BorrowedRecord> borrowRecords;

    public UserDetails(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
