package com.finguard.repository;

import com.finguard.model.Account;
import com.finguard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUser(User user);

    Optional<Account> findByAccountNumber(String accountNumber);

    boolean existsByUser(User user);

    boolean existsByAccountNumber(String accountNumber);
}