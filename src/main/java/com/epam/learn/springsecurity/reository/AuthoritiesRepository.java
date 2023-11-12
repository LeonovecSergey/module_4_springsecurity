package com.epam.learn.springsecurity.reository;

import com.epam.learn.springsecurity.model.AuthoritiesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthoritiesRepository extends JpaRepository<AuthoritiesEntity, Long> {
}
