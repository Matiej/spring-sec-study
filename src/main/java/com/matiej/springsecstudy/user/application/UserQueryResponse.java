package com.matiej.springsecstudy.user.application;

import com.matiej.springsecstudy.user.domain.Role;
import com.matiej.springsecstudy.user.domain.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserQueryResponse {
    private Long id;
    private String username;
    private LocalDateTime createdAt;
    private List<String> roles;
    private String email;
    private List<String> activeUsers;

    public static UserQueryResponse convertToResponse(UserEntity userEntity) {
        return UserQueryResponse.builder()
                .id(userEntity.getId())
                .username(userEntity.getName())
                .createdAt(userEntity.getCreatedAt())
                .roles(userEntity.getRoles().stream().map(Role::getRoleName).toList())
                .email(userEntity.getUserEmail())
                .build();
    }

    public static List<UserQueryResponse> convertToResponse(List<UserEntity> userEntityList) {
        return userEntityList.stream()
                .map(UserQueryResponse::convertToResponse)
                .toList();
    }
}
