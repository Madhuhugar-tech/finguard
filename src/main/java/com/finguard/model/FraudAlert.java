package com.finguard.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "fraud_alerts")
public class FraudAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "transaction_id", nullable = false, unique = true)
    private Transaction transaction;

    @Column(nullable = false)
    private int riskScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FraudRiskLevel riskLevel;

    @Column(nullable = false, length = 1000)
    private String reasons;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvestigationStatus investigationStatus =
            InvestigationStatus.OPEN;

    @Column(length = 2000)
    private String analystNotes;

    private LocalDateTime investigatedAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public FraudAlert() {
    }

    public Long getId() {
        return id;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public int getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(int riskScore) {
        this.riskScore = riskScore;
    }

    public FraudRiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(FraudRiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getReasons() {
        return reasons;
    }

    public void setReasons(String reasons) {
        this.reasons = reasons;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public InvestigationStatus getInvestigationStatus() {
        return investigationStatus;
    }

    public void setInvestigationStatus(
            InvestigationStatus investigationStatus) {
        this.investigationStatus = investigationStatus;
    }

    public String getAnalystNotes() {
        return analystNotes;
    }

    public void setAnalystNotes(String analystNotes) {
        this.analystNotes = analystNotes;
    }

    public LocalDateTime getInvestigatedAt() {
        return investigatedAt;
    }

    public void setInvestigatedAt(LocalDateTime investigatedAt) {
        this.investigatedAt = investigatedAt;
    }
}