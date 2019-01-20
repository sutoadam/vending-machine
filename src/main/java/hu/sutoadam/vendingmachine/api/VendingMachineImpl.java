package hu.sutoadam.vendingmachine.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import hu.sutoadam.vendingmachine.model.Coin;
import hu.sutoadam.vendingmachine.model.Product;
import hu.sutoadam.vendingmachine.model.PurchaseResult;
import hu.sutoadam.vendingmachine.model.Report;
import hu.sutoadam.vendingmachine.store.Store;

public class VendingMachineImpl implements VendingMachine {
	private Store<Coin> coinStore = new Store<>();
	private Store<Product> productStore = new Store<>();
	private List<Coin> acceptableCoins = new ArrayList<>();
	private List<Coin> userCoins = new ArrayList<>();;
	private String masterKey;
	
	public VendingMachineImpl(Options options) {
		super();
		initialize(options);
	}

	private void initialize(Options options) {
		masterKey = options.getMasterKey();
		acceptableCoins = options.getAcceptableCoins().stream()
				.sorted((coin1,coin2) -> ((Integer)coin1.getValue()).compareTo(coin2.getValue())).collect(Collectors.toList());
		acceptableCoins.stream()
			.forEach(coin -> coinStore.fill(coin, 10));
		options.getProductList().stream()
			.forEach(product -> productStore.fill(product, 10));
	}

	@Override
	public List<Product> getProductList() {
		return productStore.getTypes();
	}
	
	@Override
	public List<Coin> getAcceptableCoins() {
		return acceptableCoins;
	}

	@Override
	public Optional<Coin> putCoin(Coin coin) {
		if(isCoinValid(coin)) {
			userCoins.add(acceptableCoins.get(acceptableCoins.indexOf(coin)));
			return Optional.empty();
		} else {
			return Optional.of(coin);
		}
	}

	@Override
	public PurchaseResult purchaseProduct(Product product) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Coin> refund() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reset(String masterKey, List<Coin> coins, List<Product> products) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Report getReports(String masterKey) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private boolean isCoinValid(Coin coin) {
		return acceptableCoins.contains(coin);
	}
}
