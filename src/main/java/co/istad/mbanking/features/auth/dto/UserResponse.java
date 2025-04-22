package co.istad.mbanking.features.auth.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record UserResponse(
        String uuid,
        String name,
        String email,
        String phoneNumber,
        String gender,
        String profileImage,
        List<String> roles
) {}
