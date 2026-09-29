package com.finguard.repository;

import com.finguard.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository
        extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findAllByOrderByCreatedAtDesc();

    List<AuditLog> findByAlertIdOrderByCreatedAtDesc(
            Long alertId
    );
}