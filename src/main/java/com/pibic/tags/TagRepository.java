package com.pibic.tags;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class TagRepository implements PanacheRepository<Tag> {
    public Optional<Tag> findPublishedByName(String name) {
        return find("""
                SELECT t
                FROM Tag t
                WHERE lower(t.name) = lower(?1)
                AND t.isPublished = true
                AND EXISTS (
                            SELECT p
                            FROM Painting p
                            JOIN p.tags pt
                            WHERE pt.id = t.id
                        )
                """, name).firstResultOptional();
    }
}
