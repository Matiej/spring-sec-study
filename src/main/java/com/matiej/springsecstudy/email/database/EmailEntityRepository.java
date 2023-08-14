package com.matiej.springsecstudy.email.database;

import com.matiej.springsecstudy.email.domain.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailEntityRepository extends JpaRepository<EmailEntity, Long> {
}
