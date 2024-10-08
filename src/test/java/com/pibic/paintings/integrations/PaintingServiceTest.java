package com.pibic.paintings.integrations;

import com.pibic.churches.Address;
import com.pibic.churches.Church;
import com.pibic.churches.ChurchRepository;
import com.pibic.paintings.*;
import com.pibic.paintings.dtos.CreatePaintingDto;
import com.pibic.paintings.dtos.EngravingDto;
import com.pibic.paintings.dtos.ImageDto;
import com.pibic.paintings.dtos.UpdatePaintingDto;
import com.pibic.shared.images.Image;
import com.pibic.shared.abstraction.IStorageService;
import com.pibic.tags.Tag;
import com.pibic.tags.TagRepository;
import com.pibic.users.User;
import com.pibic.users.UserRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class PaintingServiceTest {
    @Inject
    PaintingService paintingService;
    @Inject
    PaintingRepository paintingRepository;
    @Inject
    ChurchRepository churchRepository;
    @Inject
    UserRepository userRepository;
    @Inject
    TagRepository tagRepository;
    @InjectMock
    IStorageService storageService;

    @BeforeEach
    @TestTransaction
    public void setUp(){
        paintingRepository.deleteAll();
        churchRepository.deleteAll();
        userRepository.deleteAll();
        tagRepository.deleteAll();
    }

    @Test
    @TestTransaction
    public void ShouldCreatePainting(){
        // arrange
        var user = createUser(true);
        var church = createChurch(user);
        var tag = createTag(user);
        var createPaintingDto = new CreatePaintingDto(
                "Pintura",
                "Pintura de anjo",
                "GuiGo",
                "2021-08-01",
                "bibliografia",
                "referencia",
                "Parede da igreja",
                church.getId(),
                user.getId(),
                List.of(new ImageDto("base64Painting", "Yan Tavares")),
                List.of(new EngravingDto("gravura de anjo", "base64Engraving", "Yan Tavares")),
                List.of(tag.getId(), 10L, 20L)
        );
        Mockito.when(storageService.uploadFile(Mockito.anyString(), Mockito.anyString(), Mockito.any()))
                .thenReturn("https://www.painting.com.br")
                .thenReturn("https://www.engraving.com.br");
        //act
        var paintingId = paintingService.createPainting(createPaintingDto);
        //assert
        var painting = paintingRepository.findById(paintingId);
        assertNotNull(painting);
        assertEquals(createPaintingDto.title(), painting.getTitle());
        assertEquals(createPaintingDto.description(), painting.getDescription());
        assertEquals(createPaintingDto.artisan(), painting.getArtisan());
        assertEquals(createPaintingDto.dateOfCreation(), painting.getDateOfCreation());
        assertEquals(createPaintingDto.bibliographySource(), painting.getBibliographySource());
        assertEquals(createPaintingDto.bibliographyReference(), painting.getBibliographyReference());
        assertEquals(createPaintingDto.placement(), painting.getPlacement());
        assertEquals(church.getId(), painting.getChurch().getId());
        assertEquals(user.getId(), painting.getRegisteredBy().getId());
        assertEquals(1, painting.getImages().size());
        assertEquals(1, painting.getEngravings().size());
        assertEquals(1, painting.getTags().size());
    }

    @Test
    @TestTransaction
    public void ShouldGetPaintingById(){
        //arrange
        var user = createUser(true);
        var church = createChurch(user);
        var tag = createTag(user);
        var painting = createPainting(user, church, tag);
        //act
        var paintingResponse = paintingService.getPaintingById(painting.getId());
        //assert
        assertNotNull(paintingResponse);
        assertEquals(painting.getId(), paintingResponse.id());
        assertEquals(painting.getTitle(), paintingResponse.title());
        assertEquals(painting.getDescription(), paintingResponse.description());
        assertEquals(painting.getArtisan(), paintingResponse.artisan());
    }

    @Test
    @TestTransaction
    public void ShouldReturnNotFoundExceptionWhenPaintingNotFound(){
        assertThrows(Exception.class, () -> paintingService.getPaintingById(1L));
    }

    @Test
    @TestTransaction
    public void ShouldReturnAllPublishedPainting(){
        //arrange
        var user = createUser(true);
        var church = createChurch(user);
        var tag = createTag(user);
        var painting = createPainting(user, church, tag);
        //act
        var paintings = paintingService.getPublishedPaintings();
        //assert
        assertNotNull(paintings);
        assertEquals(1, paintings.size());
        assertEquals(painting.getId(), paintings.get(0).id());
    }

    @Test
    @TestTransaction
    public void ShouldAdminUpdatePainting(){
        //arrange
        var user = createUser(true);
        var church = createChurch(user);
        var tag = createTag(user);
        var painting = createPainting(user, church, tag);
        var updatePaintingDto = new UpdatePaintingDto(
                painting.getId(),
                "Pintura 2",
                "Pintura de anjo 2",
                "GuiGo",
                "2021-08-01",
                "bibliografia",
                "referencia",
                "Parede da igreja",
                List.of("https://www.painting.com.br"),
                List.of(new ImageDto("base64Painting", "Yan Tavares")),
                List.of("https://www.engraving.com.br"),
                List.of(new EngravingDto("gravura de anjo2", "base64Engraving", "Yan Tavares")),
                List.of(tag.getId(), 10L, 20L),
                church.getId(),
                user.getId()
        );
        Mockito.doNothing()
                .when(storageService).deleteFile(Mockito.anyString(), Mockito.anyString());
        Mockito.when(storageService.uploadFile(Mockito.anyString(), Mockito.anyString(), Mockito.any()))
                .thenReturn("https://www.painting2.com.br")
                .thenReturn("https://www.engraving2.com.br");
        //act
        paintingService.updatePainting(updatePaintingDto);
        //assert
        var updatedPainting = paintingRepository.findById(painting.getId());
        assertNotNull(updatedPainting);
        assertEquals(updatePaintingDto.title(), updatedPainting.getTitle());
        assertEquals(updatePaintingDto.description(), updatedPainting.getDescription());
        assertEquals(updatePaintingDto.artisan(), updatedPainting.getArtisan());
        assertEquals(updatePaintingDto.dateOfCreation(), updatedPainting.getDateOfCreation());
        assertEquals(updatePaintingDto.bibliographySource(), updatedPainting.getBibliographySource());
        assertEquals(updatePaintingDto.bibliographyReference(), updatedPainting.getBibliographyReference());
        assertEquals(updatePaintingDto.placement(), updatedPainting.getPlacement());
        assertEquals(church.getId(), updatedPainting.getChurch().getId());
        assertEquals(user.getId(), updatedPainting.getRegisteredBy().getId());
        assertEquals(1, updatedPainting.getImages().size());
        assertEquals(1, updatedPainting.getEngravings().size());
        assertEquals(1, updatedPainting.getTags().size());
    }

    @Test
    @TestTransaction
    public void ShouldUserUpdatePainting(){
        //arrange
        var user = createUser(false);
        var church = createChurch(user);
        var tag = createTag(user);
        var painting = createPainting(user, church, tag);
        var updatePaintingDto = new UpdatePaintingDto(
                painting.getId(),
                "Pintura 2",
                "Pintura de anjo 2",
                "GuiGo",
                "2021-08-01",
                "bibliografia",
                "referencia",
                "Parede da igreja",
                List.of("https://www.painting.com.br"),
                List.of(new ImageDto("base64Painting", "Yan Tavares")),
                List.of("https://www.engraving.com.br"),
                List.of(new EngravingDto("gravura de anjo", "base64Engraving", "Yan Tavares")),
                List.of(tag.getId(), 10L, 20L),
                church.getId(),
                user.getId()
        );
        Mockito.doNothing()
                        .when(storageService).deleteFile(Mockito.anyString(), Mockito.anyString());
        Mockito.when(storageService.uploadFile(Mockito.anyString(), Mockito.anyString(), Mockito.any()))
                .thenReturn("https://www.painting2.com.br")
                .thenReturn("https://www.engraving2.com.br");
        //act
        paintingService.updatePainting(updatePaintingDto);
        //assert
        var updatedPainting = paintingRepository.findById(painting.getId());
        assertNotNull(updatedPainting);
        assertEquals(updatePaintingDto.title(), updatedPainting.getTitle());
        assertEquals(updatePaintingDto.description(), updatedPainting.getDescription());
        assertEquals(updatePaintingDto.artisan(), updatedPainting.getArtisan());
        assertEquals(updatePaintingDto.dateOfCreation(), updatedPainting.getDateOfCreation());
        assertEquals(updatePaintingDto.bibliographySource(), updatedPainting.getBibliographySource());
        assertEquals(updatePaintingDto.bibliographyReference(), updatedPainting.getBibliographyReference());
        assertEquals(updatePaintingDto.placement(), updatedPainting.getPlacement());
        assertEquals(church.getId(), updatedPainting.getChurch().getId());
        assertEquals(user.getId(), updatedPainting.getRegisteredBy().getId());
        assertEquals(1, updatedPainting.getImages().size());
        assertEquals(1, updatedPainting.getEngravings().size());
        assertEquals(1, updatedPainting.getTags().size());
        assertEquals("https://www.painting2.com.br", updatedPainting.getImages().get(0).getUrl());
        assertEquals("https://www.engraving2.com.br", updatedPainting.getEngravings().get(0).getUrl());
    }

    @Test
    @TestTransaction
    public void ShouldDeletePainting(){
        //arrange
        var user = createUser(true);
        var church = createChurch(user);
        var tag = createTag(user);
        var painting = createPainting(user, church, tag);
        Mockito.doNothing()
                .when(storageService).deleteFile(Mockito.anyString(), Mockito.anyString());
        //act
        paintingService.deletePainting(painting.getId(), user.getId());
        //assert
        var deletedPainting = paintingRepository.findById(painting.getId());
        assertNull(deletedPainting);
    }

    @Test
    @TestTransaction
    public void ShouldAddSuggestionToPublishedPainting(){
        //arrange
        var user = createUser(false);
        var church = createChurch(user);
        var tag = createTag(user);
        var painting = Painting.create(
                "Pintura",
                "Pintura de anjo",
                "GuiGo",
                "2021-08-01",
                "bibliografia",
                "referencia",
                "Parede da igreja",
                church,
                user,
                List.of(new Image("https://www.painting.com.br", "Yan Tavares")),
                List.of(new Engraving("gravura de anjo", "Yan Tavares", "https://www.engraving.com.br")),
                List.of(tag)
        );
        painting.publish();
        paintingRepository.persist(painting);
        var imagesDto = List.of(new ImageDto("base64Image", "Yan Tavares"));
        Mockito.when(storageService.uploadFile(Mockito.anyString(), Mockito.anyString(), Mockito.any()))
                .thenReturn("https://www.suggestion.com.br");
        //act
        paintingService.addSuggestion(painting.getId(), user.getId(), "reason1", imagesDto);
        //assert
        var updatedPainting = paintingRepository.findById(painting.getId());
        assertNotNull(updatedPainting);
        assertEquals(1, updatedPainting.getSuggestions().size());
        assertEquals("reason1", updatedPainting.getSuggestions().get(0).getReason());
        assertEquals("PENDING", updatedPainting.getSuggestions().get(0).getStatus());
        assertEquals("https://www.suggestion.com.br", updatedPainting.getSuggestions().get(0).getImages().get(0).getUrl());
    }

    private Painting createPainting(User user,
                                    Church church,
                                    Tag tag
    ){
        var painting = Painting.create(
                "Pintura",
                "Pintura de anjo",
                "GuiGo",
                "2021-08-01",
                "bibliografia",
                "referencia",
                "Parede da igreja",
                church,
                user,
                List.of(new Image("https://www.painting.com.br", "Yan Tavares")),
                List.of(new Engraving("gravura de anjo", "Yan Tavares", "https://www.engraving.com.br")),
                List.of(tag)
        );
        paintingRepository.persist(painting);
        return painting;
    }


    private User createUser(boolean isAdmin) {
        var user = User.create("John Doe",
                "johndoe@email.com",
                "123456", true);
        if (isAdmin) {
            user.setAdmin(true);
        }
        userRepository.persist(user);
        return user;
    }

    private Church createChurch(User user){
        var church = Church.create(
                "Igreja de São Francisco",
                new Address("Praça Frei Orlando, 90", "Brasilia",  "DF"),
                "Igreja de São Francisco de Assis, conhecida como Igreja de São Francisco, é uma igreja católica localizada na cidade de São João del-Rei, no estado de Minas Gerais, no Brasil.",
                "São João del-Rei",
                "Fontes 2",
                user,
                List.of(new Image("https://www.google.com.br", "Yan Tavares"))
        );
        churchRepository.persist(church);
        return church;
    }

    private Tag createTag(User user){
        var tag = Tag.create(
                "Anjo",
                user
        );
        tagRepository.persist(tag);
        return tag;
    }
}