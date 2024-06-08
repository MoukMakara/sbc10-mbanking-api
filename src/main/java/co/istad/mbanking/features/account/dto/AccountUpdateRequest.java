package co.istad.mbanking.features.account.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record AccountUpdateRequest(
        @NotBlank(message = "alias is required")
        String alias,

        @NotBlank(message = "actName is required")
        String actName,

        @NotBlank(message = "actNo is required")
        String actNo,

        @NotNull(message = "balance is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "balance must be greater than 0")
        BigDecimal balance,

        @NotNull(message = "transferLimit must not be null")
        @DecimalMin(value = "0.0", inclusive = false, message = "transferLimit must be greater than 0")
        BigDecimal transferLimit,

        @NotNull(message = "isHidden is required")
        Boolean isHidden
) {
}
