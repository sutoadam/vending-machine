package hu.sutoadam.vendingmachine.model;

import java.util.Map;

public class Report {
	private Map<Product,Integer> productConsumption;

	public Report(Map<Product, Integer> productConsumption) {
		super();
		this.productConsumption = productConsumption;
	}

	public Map<Product, Integer> getProductConsumption() {
		return productConsumption;
	}

	@Override
	public String toString() {
		return "Report [productConsumption=" + productConsumption + "]";
	}
}
