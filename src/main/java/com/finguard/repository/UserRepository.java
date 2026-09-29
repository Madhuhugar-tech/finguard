package com.finguard.repository;

import com.finguard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.finguard.model.Role;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    long countByRole(Role role);
}