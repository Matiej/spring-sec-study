package com.matiej.springsecstudy.email.database;

import com.matiej.springsecstudy.email.domain.EmailEntity;
import com.matiej.springsecstudy.email.domain.EmailStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailEntityRepository extends JpaRepository<EmailEntity, Long> {

        @Query(value = "SELECT * FROM email WHERE status = :status AND attempts < :attemptsLimit", nativeQuery = true)
//    @Query("SELECT e FROM email e JOIN FETCH e.user u JOIN FETCH u.emails WHERE e.status = :status AND e.attempts < :attemptsLimit")
    List<EmailEntity> findEmailsToResendByStatusAndLimit(@Param("status") String status,
                                                         @Param("attemptsLimit") int attemptsLimit);
}
