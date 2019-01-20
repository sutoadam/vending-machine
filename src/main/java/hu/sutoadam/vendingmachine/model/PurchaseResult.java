package hu.sutoadam.vendingmachine.model;

import java.util.List;
import java.util.Optional;

public class PurchaseResult {
	private PurchaseResultType result;
	private Optional<Product> product;
	private List<Coin> change;

	public PurchaseResult(PurchaseResultType result, Optional<Product> product, List<Coin> change) {
		super();
		this.result = result;
		this.product = product;
		this.change = change;
	}
	
	public PurchaseResultType getResult() {
		return result;
	}

	public Optional<Product> getProduct() {
		return product;
	}

	public List<Coin> getChange() {
		return change;
	}

	@Override
	public String toString() {
		return "PurchaseResult [result=" + result + ", product=" + product + ", change=" + change + "]";
	}
	
	
	
}
