package com.finguard.service;

import com.finguard.dto.FraudAlertResponse;
import com.finguard.dto.UpdateAlertStatusRequest;
import com.finguard.model.FraudAlert;
import com.finguard.model.InvestigationStatus;
import com.finguard.repository.FraudAlertRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FraudAlertService {

    private final FraudAlertRepository fraudAlertRepository;
    private final AuditLogService auditLogService;

    public FraudAlertService(
            FraudAlertRepository fraudAlertRepository,
            AuditLogService auditLogService) {

        this.fraudAlertRepository = fraudAlertRepository;
        this.auditLogService = auditLogService;
    }

    public List<FraudAlertResponse> getAllAlerts() {

        return fraudAlertRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public FraudAlertResponse getAlert(Long id) {

        FraudAlert alert = fraudAlertRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Fraud alert not found"));

        return toResponse(alert);
    }

    public FraudAlertResponse updateStatus(
            Long id,
            UpdateAlertStatusRequest request,
            String actorEmail) {

        if (request.getStatus() == null) {
            throw new RuntimeException(
                    "Investigation status is required");
        }

        FraudAlert alert = fraudAlertRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Fraud alert not found"));
        InvestigationStatus previousStatus =
                alert.getInvestigationStatus();

        alert.setInvestigationStatus(request.getStatus());
        alert.setAnalystNotes(request.getNotes());
        alert.setInvestigatedAt(LocalDateTime.now());

        FraudAlert savedAlert =
                fraudAlertRepository.save(alert);
        auditLogService.logStatusUpdate(
                savedAlert.getId(),
                actorEmail,
                previousStatus,
                savedAlert.getInvestigationStatus(),
                request.getNotes()
        );

        return toResponse(savedAlert);
    }

    private FraudAlertResponse toResponse(FraudAlert alert) {

        return new FraudAlertResponse(
                alert.getId(),
                alert.getTransaction().getId(),
                alert.getTransaction()
                        .getSenderAccount()
                        .getAccountNumber(),
                alert.getTransaction()
                        .getReceiverAccount()
                        .getAccountNumber(),
                alert.getTransaction().getAmount(),
                alert.getTransaction().getStatus(),
                alert.getRiskScore(),
                alert.getRiskLevel(),
                alert.getReasons(),
                alert.getInvestigationStatus(),
                alert.getAnalystNotes(),
                alert.getCreatedAt(),
                alert.getInvestigatedAt()
        );
    }
}