package com.myproject.smartinventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.myproject.smartinventory.service.AlertService;

/**
 * Injects global model attributes into every Thymeleaf page automatically.
 * This ensures shared navbar data (e.g. alert badge count) is always available
 * without needing to set it in every individual controller method.
 */
@ControllerAdvice
public class GlobalModelAdvice {

    @Autowired
    private AlertService alertService;

    @ModelAttribute("activeAlertCount")
    public int activeAlertCount() {
        return alertService.getActiveAlerts().size();
    }
}
