package org.example.librarymanagementsystem.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReturnedBookDTO {

    @NotEmpty
    @Pattern(regexp = "^(?=(?:[^0-9]*[0-9]){10}(?:(?:[^0-9]*[0-9]){3})?$)[\\d-]+$", message = "ISBN-No must be in valid format (ISBN-10 or ISBN-13)")
    String isbnNo;

    //it's return the today's date and time to store the returned time into the database
    Date returnedDate = new Date();

    public ReturnedBookDTO(String isbnNo) {
        this.isbnNo = isbnNo;
    }
}
