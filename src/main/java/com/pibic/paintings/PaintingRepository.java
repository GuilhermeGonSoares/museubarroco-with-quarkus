package com.pibic.paintings;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PaintingRepository implements PanacheRepository<Painting> {
    public List<String> getArtisans() {
        return find("select distinct artisan from Painting").project(String.class).list();
    }
}
