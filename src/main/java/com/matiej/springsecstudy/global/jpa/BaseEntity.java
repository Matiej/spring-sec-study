package com.matiej.springsecstudy.global.jpa;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

import static java.util.UUID.randomUUID;

@Getter
@Setter
@ToString
public abstract class BaseEntity {
    //implementation later when database will be implemented
//    @Id
//    @GeneratedValue
    private Long id;
    private String UUID = randomUUID().toString();
//    @CreatedDate
    private LocalDateTime createdAt;
//    @LastModifiedDate
//    private LocalDateTime lastUpdatedAt;
//    @Version
//    private long version;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(UUID, that.UUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, UUID);
    }
}
