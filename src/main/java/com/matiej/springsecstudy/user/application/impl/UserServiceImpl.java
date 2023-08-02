package com.matiej.springsecstudy.user.application.impl;

import com.matiej.springsecstudy.user.application.UserQueryResponse;
import com.matiej.springsecstudy.user.application.UserService;
import com.matiej.springsecstudy.user.controller.CreateUserCommand;
import com.matiej.springsecstudy.user.database.UserRepository;
import com.matiej.springsecstudy.user.domain.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public UserQueryResponse save(CreateUserCommand user) {
        UserEntity savedUser = repository.save(user.convertToUserEntity());

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
}