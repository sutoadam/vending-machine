package hu.sutoadam.vendingmachine.api;

import java.util.List;

import hu.sutoadam.vendingmachine.model.Coin;
import hu.sutoadam.vendingmachine.model.Product;

public class Options {
	private List<Coin> acceptableCoins;
	private List<Product> productList;
	private int numberOfItems;
	private String masterKey;

	public Options(List<Coin> acceptableCoins, List<Product> productList, int numberOfItems, String masterKey) {
		super();
		this.acceptableCoins = acceptableCoins;
		this.productList = productList;
		this.numberOfItems = numberOfItems;
		this.masterKey = masterKey;
	}

	public int getNumberOfItems() {
		return numberOfItems;
	}

	public List<Coin> getAcceptableCoins() {
		return acceptableCoins;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public String getMasterKey() {
		return masterKey;
	}
	
	
}
