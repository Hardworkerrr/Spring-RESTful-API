package com.example.app.repository;

import com.example.app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BaseUserRepository extends JpaRepository<User, Long> {
    Optional<User> getUserById(Long id);
    Page<User> getUsersByBirthDateBetween(LocalDate from, LocalDate to, PageRequest pageRequest);
}
