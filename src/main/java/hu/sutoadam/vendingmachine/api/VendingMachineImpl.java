package hu.sutoadam.vendingmachine.api;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.print.attribute.standard.MediaSize.Other;

import hu.sutoadam.vendingmachine.model.Coin;
import hu.sutoadam.vendingmachine.model.Product;
import hu.sutoadam.vendingmachine.model.PurchaseResult;
import hu.sutoadam.vendingmachine.model.PurchaseResultType;
import hu.sutoadam.vendingmachine.model.Report;
import hu.sutoadam.vendingmachine.store.Store;

public class VendingMachineImpl implements VendingMachine {
	private Store<Coin> coinStore = new Store<>();
	private Store<Product> productStore = new Store<>();
	private List<Coin> acceptableCoins = new ArrayList<>();
	private List<Coin> userCoins = new ArrayList<>();
	private String masterKey;
	
	public VendingMachineImpl(Options options) {
		super();
		initialize(options);
	}

	private void initialize(Options options) {
		masterKey = options.getMasterKey();
		acceptableCoins = options.getAcceptableCoins().stream()
				.sorted((coin1,coin2) -> Integer.compare(coin2.getValue(),coin1.getValue())).collect(Collectors.toList());
		acceptableCoins.stream()
			.forEach(coin -> coinStore.fill(coin, options.getNumberOfItems()));
		options.getProductList().stream()
			.forEach(product -> productStore.fill(product, options.getNumberOfItems()));
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
		Product validatedProduct = productStore.getItem(product);
		if(!productStore.hasItem(validatedProduct)) {
			return handleProductSoldOut(new ArrayList<Coin>(userCoins));
		}
		int balance = calculateUserBalance();
		if(validatedProduct.getPrice() > balance) {
			return handleInsufficientBalance();
		} else {
			Optional<List<Coin>> change = calculateChange(balance - validatedProduct.getPrice());
			if(change.isPresent()) {
				return handleOk(validatedProduct,change.get());
			} else {
				return handleChangeCantBeCompleted(new ArrayList<Coin>(userCoins));
			}
		}
	}

	@Override
	public List<Coin> refund() {
		List<Coin> refundedCoins = new ArrayList<>(userCoins);
		userCoins.clear();
		return refundedCoins;
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
	
	private PurchaseResult handleChangeCantBeCompleted(List<Coin> coins) {
		userCoins.clear();
		return new PurchaseResult(PurchaseResultType.CHANGE_CANT_BE_COMPLETED, Optional.empty(), coins);
	}

	private PurchaseResult handleOk(Product product, List<Coin> coins) {
		removeChangeCoinsFromStore(coins);
		productStore.removeOneQuantity(product);
		mergeUserCoinsWithCoinStore();
		userCoins.clear();
		return new PurchaseResult(PurchaseResultType.OK,Optional.of(product), coins);
	}

	private PurchaseResult handleInsufficientBalance() {
		return new PurchaseResult(PurchaseResultType.INSUFFICIENT_BALANCE, Optional.empty(), new ArrayList<>());
	}

	private PurchaseResult handleProductSoldOut(List<Coin> coins) {
		userCoins.clear();
		return new PurchaseResult(PurchaseResultType.PRODUCT_SOLD_OUT, Optional.empty(), coins);
	}
	
	private void removeChangeCoinsFromStore(List<Coin> change) {
		change.stream().forEach(coin -> coinStore.removeOneQuantity(coin));
	}

	private void mergeUserCoinsWithCoinStore() {
		userCoins.stream().forEach(coin -> coinStore.addOneQuantity(coin));
	}
	
	private boolean isCoinValid(Coin coin) {
		return acceptableCoins.contains(coin);
	}
	
	private int calculateUserBalance() {
		int balance = 0;
		for(Coin coin :userCoins) {
			balance += coin.getValue();
		}
		return balance;
	}
	
	private Optional<List<Coin>> calculateChange(int change) {
		List<Coin> coins = new ArrayList<>();
		while(change > 0) {
			boolean hasCoin = false;
			for(Coin coin : acceptableCoins) {
				if(change >= coin.getValue() && coinStore.hasItem(coin)) {
					coinStore.removeOneQuantity(coin);
					coins.add(coin);
					change -= coin.getValue();
					hasCoin = true;
					break;
				}
			}
			if(!hasCoin) {
				revertCoinStore(coins);
				return Optional.empty();
			}
		}
		return Optional.of(coins);
	}
	
	private void revertCoinStore(List<Coin> coins) {
		coins.stream().forEach(coin -> coinStore.addOneQuantity(coin));
	}
}
