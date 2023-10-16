package com.example.blogpractice.payloads;

import com.example.blogpractice.modals.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDTO {
    private String id;

    @NotBlank
    @NotEmpty
    @Size(min = 6, max = 20, message = "Title should be between 6 to 20 characters")
    private String title;
    @NotBlank
    @NotEmpty
    @Size(min = 6, max = 200, message = "Message should be between 6 to 200 characters")
    private String body;

    private User user;
}
