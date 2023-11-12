package com.epam.learn.springsecurity.reository;

import com.epam.learn.springsecurity.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("""
            select u
            from UserEntity u
            left join fetch u.authorities ae
            where u.name = :username
            """)
    Optional<UserEntity> findByName(String username);
}
