package com.matiej.springsecstudy.security;

import com.matiej.springsecstudy.user.domain.Privilege;
import com.matiej.springsecstudy.user.domain.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
public class UserEntityDetails implements UserDetails {
    private final UserEntity userEntity;

    public UserEntity getUserEntity() {
        return userEntity;
    }

    //    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return userEntity.getRoles().stream()
//                .map(Role::getRoleName)
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toSet());
//    }
//todo if we want base on privlages map privaleg names. This project uses only roles, here the code just to show what to do
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userEntity.getRoles().stream()
                .flatMap(rol -> rol.getPrivileges()
                        .stream()
                        .map(Privilege::getName))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return this.userEntity.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return userEntity.getEnabled();
    }
}
