package com.finguard.service;

import com.finguard.dto.AdminStatsResponse;
import com.finguard.model.FraudRiskLevel;
import com.finguard.model.InvestigationStatus;
import com.finguard.model.Role;
import com.finguard.model.TransactionStatus;
import com.finguard.repository.FraudAlertRepository;
import com.finguard.repository.TransactionRepository;
import com.finguard.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminStatsService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final FraudAlertRepository fraudAlertRepository;

    public AdminStatsService(
            UserRepository userRepository,
            TransactionRepository transactionRepository,
            FraudAlertRepository fraudAlertRepository) {

        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.fraudAlertRepository = fraudAlertRepository;
    }

    public AdminStatsResponse getStats() {

        long totalUsers =
                userRepository.count();

        long totalCustomers =
                userRepository.countByRole(Role.CUSTOMER);

        long totalAnalysts =
                userRepository.countByRole(Role.ANALYST);

        long totalAdmins =
                userRepository.countByRole(Role.ADMIN);

        long totalTransactions =
                transactionRepository.count();

        long successfulTransactions =
                transactionRepository
                        .countByStatus(TransactionStatus.SUCCESS);

        long blockedTransactions =
                transactionRepository
                        .countByStatus(TransactionStatus.BLOCKED);

        long suspiciousTransactions =
                transactionRepository
                        .countByRiskLevel(
                                FraudRiskLevel.SUSPICIOUS
                        );

        long totalFraudAlerts =
                fraudAlertRepository.count();

        long openInvestigations =
                fraudAlertRepository
                        .countByInvestigationStatus(
                                InvestigationStatus.OPEN
                        );

        long resolvedInvestigations =
                fraudAlertRepository
                        .countByInvestigationStatus(
                                InvestigationStatus.RESOLVED
                        );

        return new AdminStatsResponse(
                totalUsers,
                totalCustomers,
                totalAnalysts,
                totalAdmins,
                totalTransactions,
                successfulTransactions,
                blockedTransactions,
                suspiciousTransactions,
                totalFraudAlerts,
                openInvestigations,
                resolvedInvestigations
        );
    }
}