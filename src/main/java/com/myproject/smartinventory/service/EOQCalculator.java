package com.myproject.smartinventory.service;

public class EOQCalculator {

	// Safety Stock = Z x _d x underRoot(LT)
	// Z = 1.65 (for 95% service level - from SRS FR-4.1)
	// _d = standard deviation of daily demand
	// LT = lead time in days
	public double calculateSafetyStock(double demandStdDev, double leadTimeDays) {
		double Z =1.65;
		return Z * demandStdDev * Math.sqrt(leadTimeDays);
	}
	
	// EOQ = underRoot(2DS /H) -Wilson's Formula (FR-4.2)
	// D = annual demand (units per year)
	// S = ordering cost per order (in Rs)
	// H = holding cost per unit per year (in Rs)
	public double calculateEOQ(double annualDemand, double orderingCost, double holdingCost) {
		if(holdingCost <= 0) return 0;
		return Math.sqrt((2 * annualDemand * orderingCost) / holdingCost);
	}
}
