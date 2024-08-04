package com.pibic.users.controller;

public record LoginUserRequest(
        String email,
        String password
) {
}
