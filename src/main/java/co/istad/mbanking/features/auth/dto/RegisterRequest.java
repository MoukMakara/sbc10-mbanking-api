package co.istad.mbanking.features.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "Phone number is required")
        @Size(min = 9, max = 10, message = "Phone number must be between 9 to 10 digits")
        String phoneNumber,

        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Pin is required")
        @Size(min = 4, max = 4, message = "Pin must be only 4 digits")
        String pin,

        @NotBlank(message = "Password is required")
        @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
                message = "Password must contain minimum 8 characters in length, " +
                        "at least one uppercase English letter, " +
                        "at least one lowercase English letter, " +
                        "at least one digit," +
                        "at least one special character.") // Regular Expression
        String password,

        @NotBlank(message = "Confirmed Password is required")
        @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
                message = "Password must contain minimum 8 characters in length, " +
                        "at least one uppercase English letter, " +
                        "at least one lowercase English letter, " +
                        "at least one digit," +
                        "at least one special character.") // Regular Expression
        String confirmedPassword,

        @NotBlank(message = "National Card ID is required")
        String nationalCardId,

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Gender is required")
        String gender,

        @NotNull(message = "Term must be accepted")
        Boolean acceptTerm
) {
}