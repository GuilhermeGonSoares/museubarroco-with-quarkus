package com.pibic.churches.integrations;

import com.pibic.churches.ChurchRepository;
import com.pibic.churches.ChurchService;
import com.pibic.churches.dtos.ChurchImageDto;
import com.pibic.churches.dtos.CreateChurchDto;
import com.pibic.users.User;
import com.pibic.users.UserRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @BeforeEach
    @TestTransaction
    public void setup() {
        churchRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @TestTransaction
    void ShouldCreateChurch() {
        var user = createUser(true);
        var imagesDto = List.of(
                new ChurchImageDto("url", "photographer"),
                new ChurchImageDto("url2", "photographer2")
        );
        var createChurchDto = new CreateChurchDto(
                "Igreja Batista",
                "Rua 1",
                "Cidade 1",
                "DF",
                "Descrição",
                "Referências",
                user.getId(),
                imagesDto
        );
        var id = churchService.createChurch(createChurchDto);
        assertNotNull(id);
        var churchDto = churchService.getChurch(id);
        assertEquals(createChurchDto.name(), churchDto.name());
    }


    private User createUser(boolean isAdmin) {
        var user = User.create("John Doe",
                "johndoe@email.com",
                "123456", isAdmin, true);
        userRepository.persist(user);
        return user;
    }
}