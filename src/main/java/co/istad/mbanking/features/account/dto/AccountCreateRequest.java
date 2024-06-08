package co.istad.mbanking.features.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AccountCreateRequest(

        @NotBlank(message = "Account no is required")
        String actNo,

        @NotNull(message = "Balance is required")
        @Positive
        BigDecimal balance,

        @NotBlank(message = "Account type is required")
        String accountTypeAlias,

        @NotBlank(message = "Account owner is required")
        String userUuid,

        @NotBlank(message = "Account owner is required")
        Boolean isDeleted
) {
}
