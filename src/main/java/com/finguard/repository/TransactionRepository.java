package com.finguard.repository;

import com.finguard.model.Account;
import com.finguard.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import com.finguard.model.FraudRiskLevel;
import com.finguard.model.TransactionStatus;

import java.util.List;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySenderAccountOrReceiverAccountOrderByCreatedAtDesc(
            Account senderAccount,
            Account receiverAccount
    );
    long countByStatus(TransactionStatus status);

    long countByRiskLevel(FraudRiskLevel riskLevel);
}