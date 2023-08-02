package com.matiej.springsecstudy.user.application;

import com.matiej.springsecstudy.user.controller.CreateUserCommand;
import com.matiej.springsecstudy.user.domain.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserEntity> findAll();

    UserQueryResponse save(CreateUserCommand user);

    void deleteUser(Long id);

    Optional<UserQueryResponse> findById(Long id);
}