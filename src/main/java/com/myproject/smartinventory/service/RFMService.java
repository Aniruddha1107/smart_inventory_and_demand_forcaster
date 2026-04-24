package com.myproject.smartinventory.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myproject.smartinventory.entity.Customer;
import com.myproject.smartinventory.entity.Sales;
import com.myproject.smartinventory.repository.CustomerRepository;
import com.myproject.smartinventory.repository.SalesRepository;

@Service
public class RFMService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private SalesRepository salesRepository;

	public void calculateRFMDForAllCustomers() {
		List<Customer> customers = customerRepository.findAll();
		for (Customer customer : customers) {
			List<Sales> sales = salesRepository.findByCustomer_CustomerId(customer.getCustomerId());
			if (sales.isEmpty())
				continue;

			LocalDate lastPurchase = sales.stream()
					.map(Sales::getSaleDate)
					.max(LocalDate::compareTo).orElse(LocalDate.now());
			int recency = (int) ChronoUnit.DAYS.between(lastPurchase, LocalDate.now());

			int frequency = sales.size();

			BigDecimal monetary = sales.stream()
					.map(Sales::getTotalAmount)
					.filter(a -> a != null)
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			customer.setRecency(recency);
			customer.setFrequency(frequency);
			customer.setMonetaryValue(monetary);

			int rScore = recency <= 30 ? 5 : recency <= 60 ? 4 : recency <= 90 ? 3 : recency <= 180 ? 2 : 1;
			int fScore = frequency >= 10 ? 5 : frequency >= 7 ? 4 : frequency >= 4 ? 3 : frequency >= 2 ? 2 : 1;
			int mScore = monetary.compareTo(new BigDecimal("50000")) >= 0 ? 5
					: monetary.compareTo(new BigDecimal("20000")) >= 0 ? 4
							: monetary.compareTo(new BigDecimal("10000")) >= 0 ? 3
									: monetary.compareTo(new BigDecimal("5000")) >= 0 ? 2 : 1;

			double avgScore = (rScore + fScore + mScore) / 3.0;

			if (avgScore >= 4.0)
				customer.setRfmSegment("Champions");
			else if (avgScore >= 3.0)
				customer.setRfmSegment("Loyal");
			else if (avgScore >= 2.0)
				customer.setRfmSegment("At-Risk");
			else
				customer.setRfmSegment("Lost");

			customerRepository.save(customer);

		}
	}
}
