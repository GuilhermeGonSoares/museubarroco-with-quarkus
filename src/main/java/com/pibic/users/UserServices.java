package com.pibic.users;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class UserServices {

    @Inject
    UserRepository userRepository;

    @Transactional
    public User createUser(String name, String email, String password, boolean isAdmin) {
        var userWithEmail = userRepository.findByEmail(email);
        var user = User.create(name, email, password, isAdmin, userWithEmail == null);
        userRepository.persist(user);
        return user;
    }

    public List<User> listAllUsers() {
        return userRepository.listAll();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
