package com.pibic.churches;

import com.pibic.shared.Image;
import com.pibic.users.User;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Entity
@Table(name = "churches")
public class Church {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Embedded
    private Address address;
    @Column(nullable = true)
    private String description;
    @Column(nullable = true)
    private String bibliographyReferences;
    private boolean isPublished;
    @ManyToOne
    @JoinColumn(name = "registered_by")
    private User registeredBy;
    @OneToMany
    @JoinTable(name = "church_images",
            joinColumns = @JoinColumn(name = "church_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id"))
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    private List<Image> images;

    public Church() {
    }

    private Church (String name,
                    Address address,
                    String description,
                    String bibliographyReferences,
                    boolean isPublished,
                    User registeredBy,
                    List<Image> images) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.bibliographyReferences = bibliographyReferences;
        this.isPublished = isPublished;
        this.registeredBy = registeredBy;
        this.images = images;
    }

    public static Church create(String name,
                                 Address address,
                                 String description,
                                 String bibliographyReferences,
                                 User registeredBy,
                                 List<Image> images) {
        return new Church(name, address, description, bibliographyReferences,registeredBy.isAdmin(), registeredBy, images);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getBibliographyReferences() {
        return bibliographyReferences;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public User getRegisteredBy() {
        return registeredBy;
    }

    public List<Image> getImages() {
        return images;
    }
}
