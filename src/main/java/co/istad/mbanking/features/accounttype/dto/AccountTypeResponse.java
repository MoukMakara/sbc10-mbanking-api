package co.istad.mbanking.features.accounttype.dto;

public record AccountTypeResponse(
        String alias,
        String name,
        String description,
        Boolean isDeleted
) {
}
