package com.pibic.churches;

import jakarta.persistence.Embeddable;

import java.util.Set;

@Embeddable
public record Address(String street, String city, String state) {
    static final Set<String> validStates = Set.of(
            "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS",
            "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR",
            "SC", "SP", "SE", "TO"
    );

    public Address {
        if (city == null || city.trim().isEmpty())
            throw new IllegalArgumentException("City cannot be empty.");
        if (!validStates.contains(state))
            throw new IllegalArgumentException("Invalid state.");
    }
}
