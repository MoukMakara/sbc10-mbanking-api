package co.istad.mbanking.exception;

import lombok.Builder;

@Builder
public record FieldErrorResponse(
        String field,
        String detail
) {
}
