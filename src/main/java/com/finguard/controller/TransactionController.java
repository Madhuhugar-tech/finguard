package com.finguard.controller;

import com.finguard.dto.TransferRequest;
import com.finguard.model.Transaction;
import com.finguard.model.User;
import com.finguard.service.TransactionService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(
            TransactionService transactionService) {

        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public Transaction transfer(
            @RequestBody TransferRequest request,
            Authentication authentication) {

        User sender = (User) authentication.getPrincipal();

        return transactionService.transfer(sender, request);
    }

    @GetMapping("/history")
    public List<Transaction> getHistory(
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return transactionService.getTransactionHistory(user);
    }
}