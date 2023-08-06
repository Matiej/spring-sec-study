package com.matiej.springsecstudy.user.application.impl;

import com.matiej.springsecstudy.user.application.UserQueryResponse;
import com.matiej.springsecstudy.user.application.UserService;
import com.matiej.springsecstudy.user.controller.CreateUserCommand;
import com.matiej.springsecstudy.user.controller.RegisterUserCommand;
import com.matiej.springsecstudy.user.database.UserRepository;
import com.matiej.springsecstudy.user.domain.UserEntity;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public List<UserEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public UserQueryResponse save(CreateUserCommand user) {
        UserEntity savedUser = repository.save(user.convertToUserEntity(passwordEncoder));
        return UserQueryResponse.convertToResponse(savedUser);
    }

    @Override
    public void deleteUser(Long id) {
        repository.delete(id);
    }

    @Override
    public Optional<UserQueryResponse> findById(Long id) {

        return repository.findById(id).map(UserQueryResponse::convertToResponse);
    }

    @Override
    public UserQueryResponse registerNewUser(RegisterUserCommand user) {
        if(isUserExist(user.getEmail())) {
            throw new IllegalArgumentException("There is an account with email: " + user.getEmail());
        }
        UserEntity savedUser = repository.save(user.toUserEntity(passwordEncoder));
        return UserQueryResponse.convertToResponse(savedUser);
    }

    @Override
    public Optional<UserEntity> findByName(String username) {
        return repository.findByName(username);

    }

    private boolean isUserExist(String email) {
        return repository.findByEmail(email).isPresent();
    }
}
