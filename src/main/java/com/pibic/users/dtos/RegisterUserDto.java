package com.pibic.users.dtos;

public record RegisterUserDto(
    String name,
    String email,
    String password
)
{ }
