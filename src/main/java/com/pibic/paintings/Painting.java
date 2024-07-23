package com.pibic.paintings;

import com.pibic.churches.Church;
import com.pibic.shared.Image;
import com.pibic.tags.Tag;
import com.pibic.users.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "paintings")
public class Painting {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String title;
    private String description;
    @Column(nullable = false)
    private boolean isPublished;
    private String Artisan;
    private String DateOfCreation;
    private String BibliographySource;
    private String BibliographyReference;
    private String Placement;
    @OneToMany
    @JoinTable(name = "painting_images",
            joinColumns = @JoinColumn(name = "painting_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id"))
    private List<Image> images = new ArrayList<>();
    @OneToMany
    @JoinTable(name = "painting_engravings",
            joinColumns = @JoinColumn(name = "painting_id"),
            inverseJoinColumns = @JoinColumn(name = "engraving_id"))
    private List<Engraving> engravings = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "church_id")
    private Church church;
    @ManyToOne
    @JoinColumn(name = "registered_by")
    private User registeredBy;
    @ManyToMany
    @JoinTable(name = "painting_tags",
            joinColumns = @JoinColumn(name = "painting_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags = new ArrayList<>();

}
