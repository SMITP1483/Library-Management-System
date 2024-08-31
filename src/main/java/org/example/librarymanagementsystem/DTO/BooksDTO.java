package org.example.librarymanagementsystem.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BooksDTO {

    @NotNull(message = "ISBN-No cannot be null")
    @Pattern(regexp = "^(?=(?:[^0-9]*[0-9]){10}(?:(?:[^0-9]*[0-9]){3})?$)[\\d-]+$", message = "ISBN-No must be in valid format (ISBN-10 or ISBN-13)")
    // Regex for ISBN 10 or ISBN 13
    private String isbnNo;

    @NotEmpty(message = "Title should not be empty")
    @Size(max = 50, message = "Author name should not exceed 50 characters")
    private String title;

    @Size(max = 50, message = "Author name should not exceed 50 characters")
    private String authorName;

    @Min(value = 1000, message = "Publication year must be at least 1000")
    @Max(value = 2024, message = "Publication year must not be more than 2024")
    private int publicationYear;

    private boolean isAvailable;




}
