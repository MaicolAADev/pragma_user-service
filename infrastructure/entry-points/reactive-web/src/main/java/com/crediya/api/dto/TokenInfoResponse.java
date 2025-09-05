package com.crediya.api.dto;


public record TokenInfoResponse(
        String id,
        String username,
        String role,
        String email,
        String identityDocument,
        boolean valid,
        long issuedAt,
        long expiration
) {}