package hu.sutoadam.vendingmachine.model;

import java.util.List;

public class PurchaseResult {
	private Product product;
	private List<Coin> change;
	
	public PurchaseResult(Product product, List<Coin> change) {
		super();
		this.product = product;
		this.change = change;
	}

	public Product getProduct() {
		return product;
	}

	public List<Coin> getChange() {
		return change;
	}
}
