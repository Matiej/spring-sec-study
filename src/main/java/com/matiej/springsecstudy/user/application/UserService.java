package com.matiej.springsecstudy.user.application;

import com.matiej.springsecstudy.user.controller.CreateUserCommand;
import com.matiej.springsecstudy.user.controller.RegisterUserCommand;
import com.matiej.springsecstudy.user.domain.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserEntity> findAll();

    UserQueryResponse save(CreateUserCommand user);

    void deleteUser(Long id);

    Optional<UserQueryResponse> findById(Long id);

    UserQueryResponse registerNewUser(RegisterUserCommand user);

    Optional<UserEntity> findByName(String username);
}
