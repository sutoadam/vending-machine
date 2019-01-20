package hu.sutoadam.vendingmachine.model;

import java.util.Map;

public class Report {
	private Map<String,Integer> productConsumption;

	public Report(Map<String, Integer> productConsumption) {
		super();
		this.productConsumption = productConsumption;
	}

	public Map<String, Integer> getProductConsumption() {
		return productConsumption;
	}

	@Override
	public String toString() {
		return "Report [productConsumption=" + productConsumption + "]";
	}
}
