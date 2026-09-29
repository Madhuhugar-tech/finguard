package com.finguard.controller;

import com.finguard.model.Account;
import com.finguard.model.User;
import com.finguard.service.AccountService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/me")
    public Account getMyAccount(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return accountService.getAccountByUser(user);
    }
}