package hu.sutoadam.vendingmachine.factory;

import java.util.ArrayList;
import java.util.List;

import hu.sutoadam.vendingmachine.api.Options;
import hu.sutoadam.vendingmachine.api.VendingMachine;
import hu.sutoadam.vendingmachine.api.VendingMachineImpl;
import hu.sutoadam.vendingmachine.model.Coin;
import hu.sutoadam.vendingmachine.model.Product;

public class VendingMachineFactory {
	public static VendingMachine createVendingMachine() {
		return new VendingMachineImpl(getOptions());
	}
	
	private static Options getOptions() {
		List<Coin> acceptableCoins = new ArrayList<>();
		acceptableCoins.add(new Coin("penny",1));
		acceptableCoins.add(new Coin("nickel",5));
		acceptableCoins.add(new Coin("dime",10));
		acceptableCoins.add(new Coin("quarter",25));
		
		List<Product> productList = new ArrayList<>();
		productList.add(new Product("Coke",25));
		productList.add(new Product("Pepsi",35));
		productList.add(new Product("Soda",45));
		
		String masterKey = "open sesame";
		return new Options(acceptableCoins, productList, 1, masterKey);
	}
}
