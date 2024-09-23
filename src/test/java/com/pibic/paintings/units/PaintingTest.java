package com.pibic.paintings.units;

import com.pibic.churches.Address;
import com.pibic.churches.Church;
import com.pibic.paintings.Engraving;
import com.pibic.paintings.Painting;
import com.pibic.paintings.Suggestion;
import com.pibic.shared.images.Image;
import com.pibic.tags.Tag;
import com.pibic.users.User;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class PaintingTest {

    @Test
    public void ShouldAdminCreatePublishedPainting(){
        // arrange
        var user = createUser(true);
        var church = createChurch(user);
        var tag = createTag(user);
        // act
        var painting = Painting.create(
                "Anjos",
                "Anjo com trombeta",
                "Gabriel",
                "2024",
                "reference1",
                "reference2",
                "Parede 1",
                church,
                user,
                List.of(new Image("https://www.google.com.br", "Yan Tavares")),
                List.of(new Engraving("Gravura de anjo","Yan Tavares", "https://www.google.com.br")),
                List.of(tag)
        );
        // assert
        assertNotNull(painting);
        assertTrue(painting.isPublished());
        assertEquals("Anjos", painting.getTitle());
        assertEquals("Anjo com trombeta", painting.getDescription());
        assertEquals("Gabriel", painting.getArtisan());
        assertEquals("2024", painting.getDateOfCreation());
        assertEquals("reference1", painting.getBibliographySource());
        assertEquals("reference2", painting.getBibliographyReference());
        assertEquals("Parede 1", painting.getPlacement());
        assertEquals(church, painting.getChurch());
        assertEquals(user, painting.getRegisteredBy());
        assertEquals(1, painting.getTags().size());
        assertTrue(painting.getTags().contains(tag));
        assertEquals(1, painting.getImages().size());
        assertEquals(1, painting.getEngravings().size());
    }

    @Test
    public void ShouldNoAdminCreatePaintingUnPublished(){
        // arrange
        var user = createUser(false);
        var church = createChurch(user);
        var tag = createTag(user);
        // act
        var painting = Painting.create(
                "Anjos",
                "Anjo com trombeta",
                "Gabriel",
                "2024",
                "reference1",
                "reference2",
                "Parede 1",
                church,
                user,
                List.of(new Image("https://www.google.com.br", "Yan Tavares")),
                List.of(new Engraving("Gravura de anjo","Yan Tavares", "https://www.google.com.br")),
                List.of(tag)
        );
        // assert
        assertNotNull(painting);
        assertFalse(painting.isPublished());
        assertEquals("Anjos", painting.getTitle());
        assertEquals("Anjo com trombeta", painting.getDescription());
        assertEquals("Gabriel", painting.getArtisan());
        assertEquals("2024", painting.getDateOfCreation());
        assertEquals("reference1", painting.getBibliographySource());
        assertEquals("reference2", painting.getBibliographyReference());
        assertEquals("Parede 1", painting.getPlacement());
        assertEquals(church, painting.getChurch());
        assertEquals(user, painting.getRegisteredBy());
        assertEquals(1, painting.getTags().size());
        assertTrue(painting.getTags().contains(tag));
        assertEquals(1, painting.getImages().size());
        assertEquals(1, painting.getEngravings().size());
    }

    @Test
    public void ShouldAdminUpdatePublishedPainting(){
        // arrange
        var user = createUser(true);
        var church = createChurch(user);
        var tag = createTag(user);
        var painting = Painting.create(
                "Anjos",
                "Anjo com trombeta",
                "Gabriel",
                "2024",
                "reference1",
                "reference2",
                "Parede 1",
                church,
                user,
                List.of(new Image("https://www.google.com.br", "Yan Tavares")),
                List.of(new Engraving("Gravura de anjo","Yan Tavares", "https://www.engraving.com.br")),
                List.of(tag)
        );
        // act
        painting.update(
                "Anjos 2",
                "Anjo com trombeta 2",
                "Gabriel 2",
                "2023",
                "reference12",
                "reference22",
                "Parede 2",
                List.of("https://www.google.com.br"),
                List.of(new Image("https://www.youtube.com.br", "Yan Tavares")),
                List.of("https://www.engraving.com.br"),
                List.of(new Engraving("Gravura de anjo","Yan Tavares", "https://www.engraving2.com.br"),
                        new Engraving("Gravura de anjo 2","Yan Tavares", "https://www.engraving3.com.br")),
                List.of(tag),
                church,
                user
        );
        // assert
        assertNotNull(painting);
        assertTrue(painting.isPublished());
        assertEquals("Anjos 2", painting.getTitle());
        assertEquals("Anjo com trombeta 2", painting.getDescription());
        assertEquals("Gabriel 2", painting.getArtisan());
        assertEquals("2023", painting.getDateOfCreation());
        assertEquals("reference12", painting.getBibliographySource());
        assertEquals("reference22", painting.getBibliographyReference());
        assertEquals("Parede 2", painting.getPlacement());
        assertEquals(church, painting.getChurch());
        assertEquals(user, painting.getRegisteredBy());
        assertEquals(1, painting.getTags().size());
        assertTrue(painting.getTags().contains(tag));
        assertEquals(1, painting.getImages().size());
        assertEquals(2, painting.getEngravings().size());
        assertEquals("https://www.youtube.com.br", painting.getImages().get(0).getUrl());
        assertEquals("https://www.engraving2.com.br", painting.getEngravings().get(0).getUrl());
        assertEquals("https://www.engraving3.com.br", painting.getEngravings().get(1).getUrl());
    }

    @Test
    public void ShouldNotOwnerUpdatePublishedPainting() {
        // arrange
        var user = createUser(false);
        var church = createChurch(user);
        var tag = createTag(user);
        var painting = Painting.create(
                "Anjos",
                "Anjo com trombeta",
                "Gabriel",
                "2024",
                "reference1",
                "reference2",
                "Parede 1",
                church,
                user,
                List.of(new Image("https://www.google.com.br", "Yan Tavares")),
                List.of(new Engraving("Gravura de anjo", "Yan Tavares", "https://www.engraving.com.br")),
                List.of(tag)
        );
        painting.publish();
        // act
        assertThrows(IllegalArgumentException.class, () -> painting.update(
                "Anjos 2",
                "Anjo com trombeta 2",
                "Gabriel 2",
                "2023",
                "reference12",
                "reference22",
                "Parede 2",
                List.of("https://www.google.com.br"),
                List.of(new Image("https://www.youtube.com.br", "Yan Tavares")),
                List.of("https://www.engraving.com.br"),
                List.of(new Engraving("Gravura de anjo", "Yan Tavares", "https://www.engraving2.com.br"),
                        new Engraving("Gravura de anjo 2", "Yan Tavares", "https://www.engraving3.com.br")),
                List.of(tag),
                church,
                createUser(false)
        ));
    }

    @Test
    public void ShouldOwnerUpdateUnPublishedPainting(){
        // arrange
        var user = createUser(false);
        user.setId(1L);
        var church = createChurch(user);
        var tag = createTag(user);
        var painting = Painting.create(
                "Anjos",
                "Anjo com trombeta",
                "Gabriel",
                "2024",
                "reference1",
                "reference2",
                "Parede 1",
                church,
                user,
                List.of(new Image("https://www.google.com.br", "Yan Tavares")),
                List.of(new Engraving("Gravura de anjo", "Yan Tavares", "https://www.engraving.com.br")),
                List.of(tag)
        );
        // act
        painting.update(
                "Anjos 2",
                "Anjo com trombeta 2",
                "Gabriel 2",
                "2023",
                "reference12",
                "reference22",
                "Parede 2",
                List.of("https://www.google.com.br"),
                List.of(new Image("https://www.youtube.com.br", "Yan Tavares")),
                List.of("https://www.engraving.com.br"),
                List.of(new Engraving("Gravura de anjo", "Yan Tavares", "https://www.engraving2.com.br"),
                        new Engraving("Gravura de anjo 2", "Yan Tavares", "https://www.engraving3.com.br")),
                List.of(tag),
                church,
                user
        );
        // assert
        assertNotNull(painting);
        assertFalse(painting.isPublished());
        assertEquals("Anjos 2", painting.getTitle());
        assertEquals("Anjo com trombeta 2", painting.getDescription());
        assertEquals("Gabriel 2", painting.getArtisan());
        assertEquals("2023", painting.getDateOfCreation());
        assertEquals("reference12", painting.getBibliographySource());
        assertEquals("reference22", painting.getBibliographyReference());
        assertEquals("Parede 2", painting.getPlacement());
        assertEquals(church, painting.getChurch());
        assertEquals(user, painting.getRegisteredBy());
        assertEquals(1, painting.getTags().size());
        assertTrue(painting.getTags().contains(tag));
        assertEquals(1, painting.getImages().size());
        assertEquals(2, painting.getEngravings().size());
    }

    @Test
    public void NotShouldUpdatePaintingWithoutImage(){
        // arrange
        var user = createUser(true);
        var church = createChurch(user);
        var tag = createTag(user);
        var painting = Painting.create(
                "Anjos",
                "Anjo com trombeta",
                "Gabriel",
                "2024",
                "reference1",
                "reference2",
                "Parede 1",
                church,
                user,
                List.of(new Image("https://www.google.com.br", "Yan Tavares")),
                List.of(new Engraving("Gravura de anjo", "Yan Tavares", "https://www.engraving.com.br")),
                List.of(tag)
        );
        // act
        assertThrows(IllegalArgumentException.class, () -> painting.update(
                "Anjos 2",
                "Anjo com trombeta 2",
                "Gabriel 2",
                "2023",
                "reference12",
                "reference22",
                "Parede 2",
                List.of("https://www.google.com.br"),
                List.of(),
                List.of("https://www.engraving.com.br"),
                List.of(new Engraving("Gravura de anjo", "Yan Tavares", "https://www.engraving2.com.br"),
                        new Engraving("Gravura de anjo 2", "Yan Tavares", "https://www.engraving3.com.br")),
                List.of(tag),
                church,
                user
        ));
    }

    @Test
    public void NotShouldAnotherUserUpdatePainting(){
        // arrange
        var user = createUser(false);
        user.setId(1L);
        var anotherUser = createUser(false);
        anotherUser.setId(2L);
        var church = createChurch(user);
        var tag = createTag(user);
        var painting = Painting.create(
                "Anjos",
                "Anjo com trombeta",
                "Gabriel",
                "2024",
                "reference1",
                "reference2",
                "Parede 1",
                church,
                user,
                List.of(new Image("https://www.google.com.br", "Yan Tavares")),
                List.of(new Engraving("Gravura de anjo", "Yan Tavares", "https://www.engraving.com.br")),
                List.of(tag)
        );
        // act
        assertThrows(IllegalArgumentException.class, () -> painting.update(
                "Anjos 2",
                "Anjo com trombeta 2",
                "Gabriel 2",
                "2023",
                "reference12",
                "reference22",
                "Parede 2",
                List.of("https://www.google.com.br"),
                List.of(new Image("https://www.youtube.com.br", "Yan Tavares")),
                List.of("https://www.engraving.com.br"),
                List.of(new Engraving("Gravura de anjo", "Yan Tavares", "https://www.engraving2.com.br"),
                        new Engraving("Gravura de anjo 2", "Yan Tavares", "https://www.engraving3.com.br")),
                List.of(tag),
                church,
                anotherUser
        ));
    }

    @Test
    public void ShouldCommonUserAddSuggestionToPublishedPainting() {
        //arrange
        var user = createUser(false);
        user.setId(1L);
        var church = createChurch(user);
        var tag = createTag(user);
        var painting = Painting.create(
                "Anjos",
                "Anjo com trombeta",
                "Gabriel",
                "2024",
                "reference1",
                "reference2",
                "Parede 1",
                church,
                user,
                List.of(new Image("https://www.google.com.br", "Yan Tavares")),
                List.of(new Engraving("Gravura de anjo", "Yan Tavares", "https://www.engraving.com.br")),
                List.of(tag)
        );
        painting.publish();
        var suggestion = createSuggestion(user);
        //act
        painting.addSuggestion(suggestion);
        //assert
        assertEquals(1, painting.getSuggestions().size());
        assertEquals(suggestion.getReason(), painting.getSuggestions().get(0).getReason());
    }

    @Test
    public void ShouldNotAddSuggestionToUnpublishedPainting(){
        //arrange
        var user = createUser(false);
        user.setId(1L);
        var church = createChurch(user);
        var tag = createTag(user);
        var painting = Painting.create(
                "Anjos",
                "Anjo com trombeta",
                "Gabriel",
                "2024",
                "reference1",
                "reference2",
                "Parede 1",
                church,
                user,
                List.of(new Image("https://www.google.com.br", "Yan Tavares")),
                List.of(new Engraving("Gravura de anjo", "Yan Tavares", "https://www.engraving.com.br")),
                List.of(tag)
        );
        var suggestion = createSuggestion(user);

        //act and assert
        assertThrows(IllegalArgumentException.class, () -> painting.addSuggestion(suggestion));
    }

    @Test
    public void ShouldNotAddSuggestionAdminUser(){
        //arrange
        var user = createUser(true);
        user.setId(1L);
        var church = createChurch(user);
        var tag = createTag(user);
        var painting = Painting.create(
                "Anjos",
                "Anjo com trombeta",
                "Gabriel",
                "2024",
                "reference1",
                "reference2",
                "Parede 1",
                church,
                user,
                List.of(new Image("https://www.google.com.br", "Yan Tavares")),
                List.of(new Engraving("Gravura de anjo", "Yan Tavares", "https://www.engraving.com.br")),
                List.of(tag)
        );
        var suggestion = createSuggestion(user);

        //act and assert
        assertThrows(IllegalArgumentException.class, () -> painting.addSuggestion(suggestion));
    }

    @Test
    public void ShouldNotAnotherCommonUserAddSuggestion(){
        //arrange
        var user = createUser(false);
        var anotherUser = createUser(false);
        user.setId(1L);
        anotherUser.setId(2L);
        var church = createChurch(user);
        var tag = createTag(user);
        var painting = Painting.create(
                "Anjos",
                "Anjo com trombeta",
                "Gabriel",
                "2024",
                "reference1",
                "reference2",
                "Parede 1",
                church,
                user,
                List.of(new Image("https://www.google.com.br", "Yan Tavares")),
                List.of(new Engraving("Gravura de anjo", "Yan Tavares", "https://www.engraving.com.br")),
                List.of(tag)
        );
        var suggestion = createSuggestion(anotherUser);
        //act and assert
        assertThrows(IllegalArgumentException.class, () -> painting.addSuggestion(suggestion));

    }

    private User createUser(boolean isAdmin){
        var user = User.create(
                "GuiGo",
                "guigo@email.com",
                "123456",
                true
        );
        if (isAdmin)
            user.setAdmin(true);
        return user;
    }

    private Church createChurch(User user){
        return Church.create(
                "Igreja de São Francisco",
                new Address("Praça Frei Orlando, 90", "Brasilia",  "DF"),
                "Igreja de São Francisco de Assis, conhecida como Igreja de São Francisco, é uma igreja católica localizada na cidade de São João del-Rei, no estado de Minas Gerais, no Brasil.",
                "São João del-Rei",
                "Fontes 2",
                user,
                List.of(new Image("https://www.google.com.br", "Yan Tavares"))
        );
    }

    private Tag createTag(User user){
        return Tag.create(
                "Anjo",
                user
        );
    }

    private Suggestion createSuggestion(User user){
        var suggestion = new Suggestion(
                "reason",
                user,
                List.of(new Image("https://www.google.com.br", "Yan Tavares"))
        );
        suggestion.setId(1L);
        return suggestion;
    }
}