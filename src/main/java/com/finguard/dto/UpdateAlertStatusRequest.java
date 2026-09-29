package com.finguard.dto;

import com.finguard.model.InvestigationStatus;

public class UpdateAlertStatusRequest {

    private InvestigationStatus status;

    private String notes;

    public UpdateAlertStatusRequest() {
    }

    public InvestigationStatus getStatus() {
        return status;
    }

    public void setStatus(InvestigationStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}