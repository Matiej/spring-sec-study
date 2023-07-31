package com.matiej.springsecstudy.user.database;

import com.fasterxml.jackson.annotation.OptBoolean;
import com.matiej.springsecstudy.user.domain.UserEntity;
import org.apache.catalina.User;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryUserDataBase implements UserRepository{
    private Map<String, UserEntity> userEntityMap = new HashMap<>();

    public UserEntity save(UserEntity user) {
        Long userId = (long) (userEntityMap.size() + 1);
        user.setId(userId);
        userEntityMap.put(String.valueOf(userId), user);
        return user;
    }

    public List<UserEntity> findAll() {
        if(userEntityMap.isEmpty()) {
            return Collections.emptyList();
        }
        return userEntityMap.values().stream().toList();
    }

    public Optional<UserEntity> findById(Long id) {
        String userId = String.valueOf(id);
        return Optional.ofNullable(userEntityMap.get(userId));
    }

    @Override
    public void delete(Long id) {
        userEntityMap.remove(String.valueOf(id));
    }
}
