package org.example.librarymanagementsystem.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExistingUserDetailsDTO{

    @NotNull(message = "UserId can't be empty")
    private Long userId;

}
