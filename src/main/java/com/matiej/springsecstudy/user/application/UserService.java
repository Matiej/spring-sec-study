package com.matiej.springsecstudy.user.application;

import com.matiej.springsecstudy.user.controller.command.CreateUserCommand;
import com.matiej.springsecstudy.user.controller.command.ModifyUserCommand;
import com.matiej.springsecstudy.user.controller.command.RegisterUserCommand;
import com.matiej.springsecstudy.user.domain.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserEntity> findAll();

    UserQueryResponse save(CreateUserCommand user);

    void deleteUser(Long id);

    Optional<UserQueryResponse> findById(Long id);

    UserQueryResponse registerNewUser(RegisterUserCommand user);

    Optional<UserEntity> findByName(String username);
    Optional<UserQueryResponse> findByEmail(String email);

    UserQueryResponse update(ModifyUserCommand user);

    void confirmRegistration(String token);
}
