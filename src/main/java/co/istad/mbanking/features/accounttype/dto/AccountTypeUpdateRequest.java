package co.istad.mbanking.features.accounttype.dto;

public record AccountTypeUpdateRequest(

        String description,

        Boolean isDeleted

) {
}
