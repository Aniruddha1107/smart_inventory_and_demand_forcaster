package com.myproject.smartinventory.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myproject.smartinventory.entity.Alert;
import com.myproject.smartinventory.entity.AlertStatus;
import com.myproject.smartinventory.entity.AlertType;
import com.myproject.smartinventory.entity.Product;
import com.myproject.smartinventory.repository.AlertRepository;

@Service
public class AlertService {

	@Autowired
	private AlertRepository alertRepository;
	
	public void checkAndCreateAlert(Product product) {
		if(product.getQuantity() <= 0) {
			createAlert(product, AlertType.OUT_OF_STOCK);
		} else if(product.getQuantity() <= product.getSafetyStock()) {
			createAlert(product, AlertType.LOW_STOCK);
		}else {
			resolveAlert(product);
		}
		
	}
	
	public void createAlert(Product product, AlertType type) {
		var existing = alertRepository.findByProductAndStatus(product, AlertStatus.ACTIVE);
		if(existing.isEmpty()) {
			Alert alert = new Alert();
			alert.setProduct(product);
			alert.setAlertType(type);
			alertRepository.save(alert);
		}
	}

	private void resolveAlert(Product product) {
		alertRepository.findByProductAndStatus(product, AlertStatus.ACTIVE)
		.ifPresent(alert -> {
			alert.setStatus(AlertStatus.RESOLVED);
			alertRepository.save(alert);
		});
		
	}
	
	public List<Alert> getActiveAlerts(){
		return alertRepository.findByStatus(AlertStatus.ACTIVE);
	}
}
