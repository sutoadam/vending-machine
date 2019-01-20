package hu.sutoadam.vendingmachine.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import hu.sutoadam.vendingmachine.model.Coin;
import hu.sutoadam.vendingmachine.model.Product;
import hu.sutoadam.vendingmachine.model.PurchaseResult;
import hu.sutoadam.vendingmachine.model.Report;

public interface VendingMachine {
	List<Product> getProductList();
	List<Coin> getAcceptableCoins();
	Optional<Coin> putCoin(Coin coin);
	PurchaseResult purchaseProduct(Product product);
	List<Coin> refund();
	void reset(String masterKey, List<Coin> coins, List<Product> products);
	Report getReports(String masterKey);
}
