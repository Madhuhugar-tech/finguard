package com.finguard.service;

import com.finguard.dto.TransferRequest;
import com.finguard.model.Account;
import com.finguard.model.Transaction;
import com.finguard.model.TransactionStatus;
import com.finguard.model.User;
import com.finguard.repository.AccountRepository;
import com.finguard.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.finguard.model.FraudRiskLevel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final FraudDetectionService fraudDetectionService;

    public TransactionService(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            FraudDetectionService fraudDetectionService) {

        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.fraudDetectionService = fraudDetectionService;
    }

    @Transactional
    public Transaction transfer(User sender,
                                TransferRequest request) {

        Account senderAccount = accountRepository.findByUser(sender)
                .orElseThrow(() ->
                        new RuntimeException("Sender account not found"));

        Account receiverAccount =
                accountRepository
                        .findByAccountNumber(
                                request.getReceiverAccountNumber())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Receiver account not found"));

        BigDecimal amount = request.getAmount();

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new RuntimeException(
                    "Transaction amount must be greater than zero");
        }

        if (senderAccount.getId()
                .equals(receiverAccount.getId())) {

            throw new RuntimeException(
                    "Sender and receiver cannot be the same account");
        }

        if (senderAccount.getBalance()
                .compareTo(amount) < 0) {

            throw new RuntimeException(
                    "Insufficient balance");
        }
        FraudDetectionService.FraudResult fraudResult =
                fraudDetectionService.analyze(
                        senderAccount,
                        amount
                );
        if (fraudResult.riskLevel() == FraudRiskLevel.BLOCKED) {

            Transaction blockedTransaction = new Transaction();

            blockedTransaction.setSenderAccount(senderAccount);
            blockedTransaction.setReceiverAccount(receiverAccount);
            blockedTransaction.setAmount(amount);
            blockedTransaction.setStatus(TransactionStatus.BLOCKED);
            blockedTransaction.setRiskLevel(FraudRiskLevel.BLOCKED);
            blockedTransaction.setCreatedAt(LocalDateTime.now());

            Transaction savedBlockedTransaction =
                    transactionRepository.save(blockedTransaction);

            fraudDetectionService.createAlert(
                    savedBlockedTransaction,
                    fraudResult
            );

            return savedBlockedTransaction;
        }

        senderAccount.setBalance(
                senderAccount.getBalance().subtract(amount)
        );

        receiverAccount.setBalance(
                receiverAccount.getBalance().add(amount)
        );

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        Transaction transaction = new Transaction();

        transaction.setSenderAccount(senderAccount);
        transaction.setReceiverAccount(receiverAccount);
        transaction.setAmount(amount);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setRiskLevel(
                fraudResult.riskLevel()
        );
        transaction.setCreatedAt(LocalDateTime.now());

        Transaction savedTransaction =
                transactionRepository.save(transaction);

        if (fraudResult.riskLevel() != FraudRiskLevel.NORMAL) {

            fraudDetectionService.createAlert(
                    savedTransaction,
                    fraudResult
            );
        }

        return savedTransaction;
    }

    public List<Transaction> getTransactionHistory(User user) {

        Account account = accountRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        return transactionRepository
                .findBySenderAccountOrReceiverAccountOrderByCreatedAtDesc(
                        account,
                        account
                );
    }
}