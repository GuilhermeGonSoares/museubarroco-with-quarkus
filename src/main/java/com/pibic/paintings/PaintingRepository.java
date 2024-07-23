package com.pibic.paintings;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PaintingRepository implements PanacheRepository<Painting> {
}
