package com.matiej.springsecstudy.user.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ModifyUserCommand extends CreateUserCommand{
    private Long id;

}
