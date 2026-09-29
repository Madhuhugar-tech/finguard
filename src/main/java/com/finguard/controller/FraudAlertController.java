package com.finguard.controller;

import com.finguard.dto.FraudAlertResponse;
import com.finguard.dto.UpdateAlertStatusRequest;
import com.finguard.service.FraudAlertService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/analyst/alerts")
@PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
public class FraudAlertController {

    private final FraudAlertService fraudAlertService;

    public FraudAlertController(
            FraudAlertService fraudAlertService) {

        this.fraudAlertService = fraudAlertService;
    }

    @GetMapping
    public List<FraudAlertResponse> getAllAlerts() {

        return fraudAlertService.getAllAlerts();
    }

    @GetMapping("/{id}")
    public FraudAlertResponse getAlert(
            @PathVariable Long id) {

        return fraudAlertService.getAlert(id);
    }

    @PatchMapping("/{id}/status")
    public FraudAlertResponse updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateAlertStatusRequest request,
            Authentication authentication) {

        return fraudAlertService.updateStatus(
                id,
                request,
                authentication.getName()
        );
    }
}