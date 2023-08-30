package com.matiej.springsecstudy.user.database;

import com.matiej.springsecstudy.user.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Role, Long> {

}
