package com.pibic.users.dtos;

public record CreateUserDto(
    String name,
    String email,
    String password,
    boolean isAdmin
)
{ }
