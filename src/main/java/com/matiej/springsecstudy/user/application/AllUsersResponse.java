package com.matiej.springsecstudy.user.application;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class AllUsersResponse {
    private final List<UserQueryResponse> userQueryResponseList;
    private final List<String> activeUserList;
}
