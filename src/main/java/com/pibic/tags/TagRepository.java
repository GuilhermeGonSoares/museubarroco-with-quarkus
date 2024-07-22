package com.pibic.tags;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class TagRepository implements PanacheRepository<Tag> {
    public Optional<Tag> findByName(String name) {
        return find("name", name).firstResultOptional();
    }
}
