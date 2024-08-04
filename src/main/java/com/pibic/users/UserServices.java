package com.pibic.users;

import com.pibic.shared.authentication.JwtTokenGenerator;
import com.pibic.users.dtos.CredentialsDto;
import com.pibic.users.dtos.RegisterUserDto;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class UserServices {

    @Inject
    UserRepository userRepository;

    @Inject
    JwtTokenGenerator jwtTokenGenerator;

    @Transactional
    public User registerUser(RegisterUserDto registerUserDto) {
        var isEmailUnique = !userRepository.findByEmail(registerUserDto.email()).isPresent();
        var hashPassword = BcryptUtil.bcryptHash(registerUserDto.password());
        var user = User.create(
                registerUserDto.name(),
                registerUserDto.email(),
                hashPassword,
                isEmailUnique
        );
        userRepository.persist(user);
        return user;
    }

    public CredentialsDto getToken(String email, String password) {
        var user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        if (!BcryptUtil.matches(password, user.getPassword())) {
            throw new BadRequestException("Invalid password or email");
        }
        var token = jwtTokenGenerator
                .generateToken(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.isAdmin() ? "admin" : "user"
                );
        return new CredentialsDto(token);
    }

    public List<User> listAllUsers() {
        return userRepository.listAll();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
