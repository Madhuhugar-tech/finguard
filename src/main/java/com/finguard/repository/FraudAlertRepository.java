package com.finguard.repository;

import com.finguard.model.FraudAlert;
import com.finguard.model.FraudRiskLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import com.finguard.model.InvestigationStatus;

import java.util.List;

public interface FraudAlertRepository
        extends JpaRepository<FraudAlert, Long> {

    List<FraudAlert> findByRiskLevelOrderByCreatedAtDesc(
            FraudRiskLevel riskLevel
    );

    List<FraudAlert> findAllByOrderByCreatedAtDesc();
    long countByInvestigationStatus(
            InvestigationStatus status
    );
}