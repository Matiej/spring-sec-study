package com.matiej.springsecstudy.user.database;

import com.matiej.springsecstudy.user.domain.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    UserEntity save(UserEntity user);
    List<UserEntity> findAll();
    Optional<UserEntity> findById(Long id);
    void delete(Long id);
}
