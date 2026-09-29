package com.finguard.service;

import com.finguard.model.AuditAction;
import com.finguard.model.AuditLog;
import com.finguard.model.InvestigationStatus;
import com.finguard.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(
            AuditLogRepository auditLogRepository) {

        this.auditLogRepository = auditLogRepository;
    }

    public void logAlertCreated(
            Long alertId,
            String actorEmail) {

        AuditLog auditLog = new AuditLog();

        auditLog.setAlertId(alertId);
        auditLog.setActorEmail(actorEmail);
        auditLog.setAction(AuditAction.ALERT_CREATED);
        auditLog.setNotes("Fraud alert created");
        auditLog.setCreatedAt(LocalDateTime.now());

        auditLogRepository.save(auditLog);
    }

    public void logStatusUpdate(
            Long alertId,
            String actorEmail,
            InvestigationStatus previousStatus,
            InvestigationStatus newStatus,
            String notes) {

        AuditLog auditLog = new AuditLog();

        auditLog.setAlertId(alertId);
        auditLog.setActorEmail(actorEmail);
        auditLog.setAction(AuditAction.STATUS_UPDATED);
        auditLog.setPreviousStatus(previousStatus);
        auditLog.setNewStatus(newStatus);
        auditLog.setNotes(notes);
        auditLog.setCreatedAt(LocalDateTime.now());

        auditLogRepository.save(auditLog);
    }

    public List<AuditLog> getAllLogs() {

        return auditLogRepository
                .findAllByOrderByCreatedAtDesc();
    }

    public List<AuditLog> getLogsForAlert(Long alertId) {

        return auditLogRepository
                .findByAlertIdOrderByCreatedAtDesc(alertId);
    }
}