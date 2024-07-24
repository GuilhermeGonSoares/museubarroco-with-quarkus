package com.pibic.users.controller;

public record CreateUserRequest(
    String name,
    String email,
    String password,
    boolean admin
) {
}
