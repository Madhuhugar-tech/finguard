package com.finguard.controller;

import com.finguard.model.AuditLog;
import com.finguard.service.AuditLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analyst/audit-logs")
@PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(
            AuditLogService auditLogService) {

        this.auditLogService = auditLogService;
    }

    @GetMapping
    public List<AuditLog> getAllLogs() {

        return auditLogService.getAllLogs();
    }

    @GetMapping("/alert/{alertId}")
    public List<AuditLog> getLogsForAlert(
            @PathVariable Long alertId) {

        return auditLogService.getLogsForAlert(alertId);
    }
}