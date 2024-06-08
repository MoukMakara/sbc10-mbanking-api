package co.istad.mbanking.features.account.dto;

import jakarta.validation.constraints.NotNull;

public record AccountSoftDeleteRequest(
        @NotNull(message = "isDeleted is required")
        Boolean isDeleted
) {
}
