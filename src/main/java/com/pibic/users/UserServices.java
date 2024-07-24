package com.pibic.users;

import com.pibic.users.dtos.CreateUserDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class UserServices {

    @Inject
    UserRepository userRepository;

    @Transactional
    public User createUser(CreateUserDto createUserDto) {
        var isEmailUnique = !userRepository.findByEmail(createUserDto.email()).isPresent();
        var user = User.create(
                createUserDto.name(),
                createUserDto.email(),
                createUserDto.password(),
                createUserDto.isAdmin(),
                isEmailUnique
        );
        userRepository.persist(user);
        return user;
    }

    public List<User> listAllUsers() {
        return userRepository.listAll();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
