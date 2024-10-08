package com.pibic.churches.integrations;

import com.pibic.churches.ChurchRepository;
import com.pibic.churches.ChurchService;
import com.pibic.churches.dtos.ChurchImageDto;
import com.pibic.churches.dtos.CreateChurchDto;
import com.pibic.churches.dtos.UpdateChurchDto;
import com.pibic.shared.abstraction.IStorageService;
import com.pibic.users.User;
import com.pibic.users.UserRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ChurchServiceTest {
    @Inject
    ChurchService churchService;
    @Inject
    UserRepository userRepository;
    @Inject
    ChurchRepository churchRepository;
    @InjectMock
    IStorageService storageService;

    @BeforeEach
    @TestTransaction
    public void setup() {
        churchRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @TestTransaction
    void ShouldCreateChurchAndGet() {
        var user = createUser(true);
        var imagesDto = List.of(
                new ChurchImageDto("base64Image", "photographer"),
                new ChurchImageDto("url2", "photographer2")
        );
        var createChurchDto = new CreateChurchDto(
                "Igreja Batista",
                "Descrição",
                "Referências",
                "fontes",
                "Rua 1",
                "Cidade 1",
                "DF",
                user.getId(),
                imagesDto
        );
        Mockito.when(storageService.uploadFile(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn("base64Image");
        var id = churchService.createChurch(createChurchDto);
        assertNotNull(id);
        var churchDto = churchService.getChurch(id);
        assertEquals(createChurchDto.name(), churchDto.name());
        assertEquals(createChurchDto.description(), churchDto.description());
        assertEquals(createChurchDto.images().size(), churchDto.images().size());
    }

    @Test
    @TestTransaction
    public void ShouldNotCreateChurchWithNoImage(){
        var user = createUser(true);
        var imagesDto = new ArrayList<ChurchImageDto>();
        var createChurchDto = new CreateChurchDto(
                "Igreja Batista",
                "Descrição",
                "Referências",
                "fontes",
                "Rua 1",
                "Cidade 1",
                "DF",
                user.getId(),
                imagesDto
        );
        assertThrows(IllegalArgumentException.class, () -> churchService.createChurch(createChurchDto));

    }

    @Test
    @TestTransaction
    public void ShouldGetAllChurches(){
        var user = createUser(true);
        var imagesDto = List.of(
                new ChurchImageDto("base64Image", "photographer"),
                new ChurchImageDto("url2", "photographer2")
        );
        var createChurchDto = new CreateChurchDto(
                "Igreja Batista",
                "Descrição",
                "Referências",
                "fontes",
                "Rua 1",
                "Cidade 1",
                "DF",
                user.getId(),
                imagesDto
        );
        var id = churchService.createChurch(createChurchDto);
        var id2 = churchService.createChurch(createChurchDto);
        var churches = churchService.getChurches(null);
        assertEquals(2, churches.size());
        assertEquals(createChurchDto.name(), churches.get(0).name());
    }

    @Test
    @TestTransaction
    public void ShouldAdminUpdateChurch(){
        var user = createUser(true);
        var imagesDto = new ArrayList<>(List.of(
                new ChurchImageDto("url1", "photographer"),
                new ChurchImageDto("url2", "photographer2")
        ));
        var createChurchDto = new CreateChurchDto(
                "Igreja Batista",
                "Descrição",
                "Referências",
                "fontes",
                "Rua 1",
                "Cidade 1",
                "DF",
                user.getId(),
                imagesDto
        );
        Mockito.when(storageService.uploadFile(Mockito.anyString(), Mockito.anyString(), Mockito.any()))
                .thenReturn("url1")
                .thenReturn("url2")
                .thenReturn("url3");
        Mockito.doNothing().when(storageService).deleteFile(Mockito.anyString(), Mockito.anyString());
        var id = churchService.createChurch(createChurchDto);
        var updateChurchDto = new UpdateChurchDto(
                id,
                "Igreja Batista 2",
                "Descrição 2",
                "Referências 2",
                "fontes 2",
                "Rua 2",
                "Cidade 2",
                "GO",
                new ArrayList<>(List.of("url1", "url2")),
                new ArrayList<>(List.of(new ChurchImageDto("url3", "photographer3"))),
                user.getId()
        );
        churchService.updateChurch(updateChurchDto);
        var updatedChurchDto = churchService.getChurch(id);
        assertEquals(updateChurchDto.name(), updatedChurchDto.name());
        assertEquals(updateChurchDto.description(), updatedChurchDto.description());
        assertEquals(updateChurchDto.images().size(), updatedChurchDto.images().size());
    }

    @Test
    @TestTransaction
    public void ShouldNotUpdateChurchWithEmptyImages(){
        var user = createUser(true);
        var imagesDto = List.of(
                new ChurchImageDto("base64Image", "photographer"),
                new ChurchImageDto("url2", "photographer2")
        );
        var createChurchDto = new CreateChurchDto(
                "Igreja Batista",
                "Descrição",
                "Referências",
                "fontes",
                "Rua 1",
                "Cidade 1",
                "DF",
                user.getId(),
                imagesDto
        );
        Mockito.when(storageService.uploadFile(Mockito.anyString(), Mockito.anyString(), Mockito.any()))
                .thenReturn("base64Image")
                .thenReturn("url2");
        var id = churchService.createChurch(createChurchDto);
        var updateChurchDto = new UpdateChurchDto(
                id,
                "Igreja Batista 2",
                "Descrição 2",
                "Referências 2",
                "Fontes 2",
                "Rua 2",
                "Cidade 2",
                "GO",
                List.of("base64Image", "url2"),
                List.of(),
                user.getId()
        );
        assertThrows(IllegalArgumentException.class, () -> churchService.updateChurch(updateChurchDto));
    }

    @Test
    @TestTransaction
    public void ShouldNotUpdateChurchWithNonAdminWhenNoOwnerOrWhenPublished(){
        var user = createUser(false);
        var anotherUser = createUser(false);
        var imagesDto = List.of(
                new ChurchImageDto("base64Image", "photographer"),
                new ChurchImageDto("url2", "photographer2")
        );
        var createChurchDto = new CreateChurchDto(
                "Igreja Batista",
                "Descrição",
                "Referências",
                "fontes",
                "Rua 1",
                "Cidade 1",
                "DF",
                user.getId(),
                imagesDto
        );
        Mockito.when(storageService.uploadFile(Mockito.anyString(), Mockito.anyString(), Mockito.any()))
                .thenReturn("base64Image")
                .thenReturn("url2")
                .thenReturn("url3");
        var id = churchService.createChurch(createChurchDto);
        var updateChurchDto = new UpdateChurchDto(
                id,
                "Igreja Batista 2",
                "Descrição 2",
                "Referências 2",
                "Fontes 2",
                "Rua 2",
                "Cidade 2",
                "GO",
                List.of("base64Image", "url2"),
                List.of(new ChurchImageDto("url3", "photographer3")),
                anotherUser.getId()
        );
        assertThrows(IllegalStateException.class, () -> churchService.updateChurch(updateChurchDto));
        var church = churchRepository.findById(id);
        church.publish();
        churchRepository.persist(church);
        var updateChurchDto2 = new UpdateChurchDto(
                id,
                "Igreja Batista 2",
                "Descrição 2",
                "Referências 2",
                "Fontes 2",
                "Rua 2",
                "Cidade 2",
                "GO",
                List.of("base64Image", "url2"),
                List.of(new ChurchImageDto("url3", "photographer3")),
                user.getId()
        );
        assertThrows(IllegalStateException.class, () -> churchService.updateChurch(updateChurchDto2));
    }

    @Test
    @TestTransaction
    public void ShouldAdminDeleteChurch(){
        var user = createUser(true);
        var imagesDto = List.of(
                new ChurchImageDto("base64Image", "photographer"),
                new ChurchImageDto("url2", "photographer2")
        );
        var createChurchDto = new CreateChurchDto(
                "Igreja Batista",
                "Descrição",
                "Referências",
                "fontes",
                "Rua 1",
                "Cidade 1",
                "DF",
                user.getId(),
                imagesDto
        );
        Mockito.when(storageService.uploadFile(Mockito.anyString(), Mockito.anyString(), Mockito.any()))
                .thenReturn("base64Image")
                .thenReturn("url2");
        Mockito.doNothing().when(storageService).deleteFile(Mockito.anyString(), Mockito.anyString());
        var id = churchService.createChurch(createChurchDto);
        churchService.deleteChurch(id, user.getId());
        assertThrows(NotFoundException.class, () -> churchService.getChurch(id));
    }

    @Test
    @TestTransaction
    public void NoAdminShouldNotDeletePublishedChurchAndNotDeleteChurchOfAnotherUser(){
        var user = createUser(false);
        var anotherUser = createUser(false);
        var imagesDto = List.of(
                new ChurchImageDto("base64Image", "photographer"),
                new ChurchImageDto("url2", "photographer2")
        );
        var createChurchDto = new CreateChurchDto(
                "Igreja Batista",
                "Descrição",
                "Referências",
                "fontes",
                "Rua 1",
                "Cidade 1",
                "DF",
                user.getId(),
                imagesDto
        );
        Mockito.when(storageService.uploadFile(Mockito.anyString(), Mockito.anyString(), Mockito.any()))
                .thenReturn("base64Image")
                .thenReturn("url2");
        Mockito.doNothing().when(storageService).deleteFile(Mockito.anyString(), Mockito.anyString());
        var id = churchService.createChurch(createChurchDto);
        assertThrows(IllegalStateException.class, () -> churchService.deleteChurch(id, anotherUser.getId()));
        var church = churchRepository.findById(id);
        church.publish();
        churchRepository.persist(church);
        assertThrows(IllegalStateException.class, () -> churchService.deleteChurch(id, user.getId()));
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
}