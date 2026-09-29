package com.finguard.service;

import com.finguard.model.Account;
import com.finguard.model.FraudAlert;
import com.finguard.model.FraudRiskLevel;
import com.finguard.model.Transaction;
import com.finguard.repository.FraudAlertRepository;
import com.finguard.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FraudDetectionService {

    private final TransactionRepository transactionRepository;
    private final FraudAlertRepository fraudAlertRepository;
    private final AuditLogService auditLogService;

    public FraudDetectionService(
            TransactionRepository transactionRepository,
            FraudAlertRepository fraudAlertRepository,
            AuditLogService auditLogService) {

        this.transactionRepository = transactionRepository;
        this.fraudAlertRepository = fraudAlertRepository;
        this.auditLogService = auditLogService;
    }

    public FraudResult analyze(
            Account senderAccount,
            BigDecimal amount) {

        int riskScore = 0;
        StringBuilder reasons = new StringBuilder();

        // Rule 1: Large transaction
        if (amount.compareTo(new BigDecimal("5000")) >= 0) {

            riskScore += 40;

            reasons.append(
                    "Large transaction amount; "
            );
        }

        // Rule 2: Transaction velocity
        LocalDateTime fiveMinutesAgo =
                LocalDateTime.now().minusMinutes(5);

        List<Transaction> recentTransactions =
                transactionRepository
                        .findBySenderAccountOrReceiverAccountOrderByCreatedAtDesc(
                                senderAccount,
                                senderAccount
                        )
                        .stream()
                        .filter(transaction ->
                                transaction.getSenderAccount()
                                        .getId()
                                        .equals(senderAccount.getId())
                                        &&
                                        transaction.getCreatedAt()
                                                .isAfter(fiveMinutesAgo)
                        )
                        .toList();

        if (recentTransactions.size() >= 3) {

            riskScore += 30;

            reasons.append(
                    "High transaction frequency; "
            );
        }

        FraudRiskLevel riskLevel;

        if (riskScore >= 60) {

            riskLevel = FraudRiskLevel.BLOCKED;

        } else if (riskScore >= 30) {

            riskLevel = FraudRiskLevel.SUSPICIOUS;

        } else {

            riskLevel = FraudRiskLevel.NORMAL;
        }

        return new FraudResult(
                riskScore,
                riskLevel,
                reasons.length() > 0
                        ? reasons.toString()
                        : "No suspicious indicators detected"
        );
    }

    public FraudAlert createAlert(
            Transaction transaction,
            FraudResult result) {

        FraudAlert alert = new FraudAlert();

        alert.setTransaction(transaction);
        alert.setRiskScore(result.riskScore());
        alert.setRiskLevel(result.riskLevel());
        alert.setReasons(result.reasons());
        alert.setCreatedAt(LocalDateTime.now());

        FraudAlert savedAlert =
                fraudAlertRepository.save(alert);

        auditLogService.logAlertCreated(
                savedAlert.getId(),
                transaction.getSenderAccount()
                        .getUser()
                        .getEmail()
        );

        return savedAlert;
    }

    public record FraudResult(
            int riskScore,
            FraudRiskLevel riskLevel,
            String reasons
    ) {
    }
}