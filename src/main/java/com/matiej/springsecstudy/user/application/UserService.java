package com.matiej.springsecstudy.user.application;

import com.matiej.springsecstudy.user.controller.command.CreateUserCommand;
import com.matiej.springsecstudy.user.controller.command.ModifyUserCommand;
import com.matiej.springsecstudy.user.domain.UserEntity;
import com.matiej.springsecstudy.user.domain.UserToken;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {
    AllUsersResponse findAll();

    UserQueryResponse save(CreateUserCommand user);

    void save(UserEntity userEntity);

    void deleteUser(Long id);

    Optional<UserQueryResponse> findById(Long id);

    UserQueryResponse registerNewUser(CreateUserCommand user, HttpServletRequest request);

    Optional<UserEntity> findByName(String username);

    Optional<UserQueryResponse> findByEmail(String email);

    UserQueryResponse update(ModifyUserCommand user);

    void confirmRegistration(String token);

    void resetPassword(String userEmail, HttpServletRequest request);

    Optional<UserToken> getPasswordResetToken(String token);

    void changeUserPassword(UserEntity user, String password);
}
