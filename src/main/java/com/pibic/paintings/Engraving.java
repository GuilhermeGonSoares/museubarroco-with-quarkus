package com.pibic.paintings;

import jakarta.persistence.*;

@Entity
@Table(name = "engravings")
public class Engraving {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String createdBy;
    @Column(nullable = false)
    private String url;

    public Engraving() {
    }

    public Engraving(String name, String createdBy, String url) {
        this.name = name;
        this.createdBy = createdBy;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUrl() {
        return url;
    }
}
