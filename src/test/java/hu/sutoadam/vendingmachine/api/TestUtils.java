package hu.sutoadam.vendingmachine.api;

import java.util.ArrayList;
import java.util.List;

import hu.sutoadam.vendingmachine.model.Coin;
import hu.sutoadam.vendingmachine.model.Product;

public class TestUtils {
	public static List<Product> getExpectedProducts(){
		List<Product> expectedProducts = new ArrayList<>();
		expectedProducts.add(new Product("Coke",25));
		expectedProducts.add(new Product("Pepsi",35));
		expectedProducts.add(new Product("Soda",45));
		return expectedProducts;
	}
	
	public static List<Coin> getExpectedCoins(){
		List<Coin> expectedCoins = new ArrayList<>();
		expectedCoins.add(new Coin("penny",1));
		expectedCoins.add(new Coin("nickel",5));
		expectedCoins.add(new Coin("dime",10));
		expectedCoins.add(new Coin("quarter",25));
		return expectedCoins;
	}
}
