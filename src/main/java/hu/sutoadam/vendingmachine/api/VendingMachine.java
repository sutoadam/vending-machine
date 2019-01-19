package hu.sutoadam.vendingmachine.api;

import java.util.List;
import java.util.Map;

import hu.sutoadam.vendingmachine.model.Coin;
import hu.sutoadam.vendingmachine.model.Product;
import hu.sutoadam.vendingmachine.model.PurchaseResult;
import hu.sutoadam.vendingmachine.model.Report;

public interface VendingMachine {
	List<Product> getProductList();
	void putCoin(Map<String,Integer> coins);
	PurchaseResult purchaseProduct(String productName);
	List<Coin> refund();
	void reset(String masterKey);
	Report getReports(String masterKey);
}
