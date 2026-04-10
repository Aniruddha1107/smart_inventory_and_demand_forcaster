package com.myproject.smartinventory.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.myproject.smartinventory.entity.Forecast;
import com.myproject.smartinventory.entity.Product;
import com.myproject.smartinventory.entity.Sales;
import com.myproject.smartinventory.repository.ForecastRepository;
import com.myproject.smartinventory.repository.SalesRepository;


@Service
public class ForecastService {

	private SalesRepository salesRepository;
	
	private ForecastRepository forecastRepository;
	
	private RestTemplate restTemplate;
	
	@Value("${forecast.api.url}")
	private String forecastApiUrl;
	
	public List<Forecast> generateForecast(Product product){
		LocalDate endDate = LocalDate.now();
		LocalDate startDate = endDate.minusDays(365);
		List<Sales> salesHistory = salesRepository
				.findByProductAndSaleDateBetween(product, startDate, endDate);
		
		List<Map<String, Object>> salesData = new ArrayList<>();
		for(Sales sale : salesHistory) {
			Map<String, Object> entry =new HashMap<>();
			entry.put("date", sale.getSaleDate().toString());
			entry.put("quantity", sale.getQuantitySold());
			salesData.add(entry);
		}
		
		Map<String, Object> requestBody =new HashMap<>();
		requestBody.put("product_id", product.getProductId());
		requestBody.put("sales_data", salesData);
		
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> response = restTemplate.postForObject(forecastApiUrl, requestBody, Map.class);
			
			if(response != null) {
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> predictions=(List<Map<String, Object>>) response.get("predictions");
				Double mape= ((Number) response.get("mape_score")).doubleValue();
				String model = (String) response.get("model_used");
				
				List<Forecast> forecasts = new ArrayList<>();
				for(Map<String, Object> pred: predictions) {
					Forecast f=new Forecast();
					f.setProduct(product);
					f.setForecastDate(LocalDate.parse((String) pred.get("date")));
					f.setPredictedDemand(((Number) pred.get("predicted_demand")).doubleValue());
					f.setMapeScore(mape);
					f.setModelUsed(model);
					forecasts.add(f);
				}
				return forecastRepository.saveAll(forecasts);
			}
		}catch (Exception e) {
			System.err.println("Forecast Service unabailable: "+e.getMessage());
		}
		return List.of();
	}
	
	public List<Forecast> getForecastsForProduct(Product product){
		return forecastRepository.findByProductOrderByForecastDateAsc(product);
	}
}
