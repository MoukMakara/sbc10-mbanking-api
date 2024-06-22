package co.istad.mbanking.features.auth.dto;

public record AuthResponse(

        // Token Type
        String tokenType,

        String accessToken,

        String refreshToken

) {
}