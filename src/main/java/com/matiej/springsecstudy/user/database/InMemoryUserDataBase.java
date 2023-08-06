//package com.matiej.springsecstudy.user.database;
//
//import com.fasterxml.jackson.annotation.OptBoolean;
//import com.matiej.springsecstudy.user.domain.UserEntity;
//import org.apache.catalina.User;
//import org.springframework.stereotype.Component;
//
//import java.util.*;
//
//@Component
//public class InMemoryUserDataBase implements UserRepository {
//    private Map<String, UserEntity> userEntityMap = new HashMap<>();
//
//    public UserEntity save(UserEntity user) {
//        if (user.getId() != null) {
//            userEntityMap.put(String.valueOf(user.getId()), user);
//
//        } else {
//            Long userId = (long) (userEntityMap.size() + 1);
//            userEntityMap.put(String.valueOf(userId), user);
//            user.setId(userId);
//        }
//        return user;
//    }
//
//    public List<UserEntity> findAll() {
//        if (userEntityMap.isEmpty()) {
//            return Collections.emptyList();
//        }
//        return userEntityMap.values().stream().toList();
//    }
//
//    public Optional<UserEntity> findById(Long id) {
//        String userId = String.valueOf(id);
//        return Optional.ofNullable(userEntityMap.get(userId));
//    }
//
//    @Override
//    public void delete(Long id) {
//        userEntityMap.remove(String.valueOf(id));
//    }
//
//    @Override
//    public Optional<UserEntity> findByEmail(String email) {
//        return userEntityMap.values()
//                .stream()
//                .filter(p-> email.equalsIgnoreCase(p.getEmail()))
//                .findAny();
//    }
//
//    @Override
//    public Optional<UserEntity> findByName(String username) {
//        return userEntityMap.values()
//                .stream()
//                .filter(p -> username.equalsIgnoreCase(p.getUsername()))
//                .findAny();
//
//    }
//}
