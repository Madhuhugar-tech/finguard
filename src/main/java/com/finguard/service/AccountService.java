package com.finguard.service;

import com.finguard.model.Account;
import com.finguard.model.User;
import com.finguard.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(User user) {

        if (accountRepository.existsByUser(user)) {
            throw new RuntimeException("User already has an account");
        }

        Account account = new Account();

        account.setAccountNumber(generateAccountNumber());
        account.setUser(user);
        account.setBalance(BigDecimal.ZERO);
        account.setCreatedAt(LocalDateTime.now());

        return accountRepository.save(account);
    }

    public Account getAccountByUser(User user) {

        return accountRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));
    }

    private String generateAccountNumber() {

        Random random = new Random();

        return "FG" + (10000000L + random.nextLong(90000000L));
    }
}