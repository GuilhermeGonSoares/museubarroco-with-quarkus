package com.pibic.shared.authentication;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;


@ApplicationScoped
public class JwtTokenGenerator {

    public String generateToken(
            Long id,
            String name,
            String email,
            String role
    ) {
        return Jwt.issuer("https://pibic.com/issuer")
                .upn(email)
                .claim("id", id)
                .claim("name", name)
                .expiresAt(Instant.now().plusSeconds(86400))
                .groups(role)
                .sign();
    }
}
