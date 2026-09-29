package com.finguard.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long alertId;

    @Column(nullable = false)
    private String actorEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditAction action;

    @Enumerated(EnumType.STRING)
    private InvestigationStatus previousStatus;

    @Enumerated(EnumType.STRING)
    private InvestigationStatus newStatus;

    @Column(length = 2000)
    private String notes;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public AuditLog() {
    }

    public Long getId() {
        return id;
    }

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public String getActorEmail() {
        return actorEmail;
    }

    public void setActorEmail(String actorEmail) {
        this.actorEmail = actorEmail;
    }

    public AuditAction getAction() {
        return action;
    }

    public void setAction(AuditAction action) {
        this.action = action;
    }

    public InvestigationStatus getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(
            InvestigationStatus previousStatus) {
        this.previousStatus = previousStatus;
    }

    public InvestigationStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(
            InvestigationStatus newStatus) {
        this.newStatus = newStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}