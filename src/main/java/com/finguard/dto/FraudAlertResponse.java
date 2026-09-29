package com.finguard.dto;

import com.finguard.model.FraudRiskLevel;
import com.finguard.model.InvestigationStatus;
import com.finguard.model.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FraudAlertResponse(

        Long alertId,

        Long transactionId,

        String senderAccountNumber,

        String receiverAccountNumber,

        BigDecimal amount,

        TransactionStatus transactionStatus,

        int riskScore,

        FraudRiskLevel riskLevel,

        String reasons,

        InvestigationStatus investigationStatus,

        String analystNotes,

        LocalDateTime createdAt,

        LocalDateTime investigatedAt
) {
}