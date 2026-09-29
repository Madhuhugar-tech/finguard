package com.finguard.dto;

public record AdminStatsResponse(

        long totalUsers,

        long totalCustomers,

        long totalAnalysts,

        long totalAdmins,

        long totalTransactions,

        long successfulTransactions,

        long blockedTransactions,

        long suspiciousTransactions,

        long totalFraudAlerts,

        long openInvestigations,

        long resolvedInvestigations

) {
}