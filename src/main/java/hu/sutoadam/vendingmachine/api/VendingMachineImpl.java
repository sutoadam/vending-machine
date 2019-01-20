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
		if(!productStore.hasItem(product)) {
			return finalizePurchase(PurchaseResultType.PRODUCT_SOLD_OUT, Optional.empty(), userCoins);
		}
		int balance = getBalanceFromUserCoins();
		Product validatedProduct = productStore.getItem(product);
		if(validatedProduct.getPrice() > balance) {
			return finalizePurchase(PurchaseResultType.INSUFFICIENT_BALANCE, Optional.empty(), new ArrayList<>());
		} else {
			Optional<List<Coin>> change = calculateChange(balance - validatedProduct.getPrice());
			if(change.isPresent()) {
				return finalizePurchase(PurchaseResultType.OK, Optional.of(validatedProduct), change.get());
			} else {
				return finalizePurchase(PurchaseResultType.CHANGE_CANT_BE_COMPLETED, Optional.empty(), userCoins);
			}
		}
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
	
	private PurchaseResult finalizePurchase(PurchaseResultType result,Optional<Product> product,List<Coin> change) {
		if(result.equals(PurchaseResultType.OK)) {
			removeChangeCoinsFromStore(change);
			productStore.removeOneQuantity(product.get());
			mergeUsersCoinsWithCoinStore();
			List<Coin> coinReturns = new ArrayList<>(change);
			userCoins.clear();
			return new PurchaseResult(result,product, coinReturns);
		} else if(result.equals(PurchaseResultType.PRODUCT_SOLD_OUT)) {
			List<Coin> coinReturns = new ArrayList<>(change);
			userCoins.clear();
			return new PurchaseResult(result, Optional.empty(), coinReturns);
		} else if(result.equals(PurchaseResultType.INSUFFICIENT_BALANCE)) {
			return new PurchaseResult(result, Optional.empty(), change);
		} else {
			List<Coin> coinReturns = new ArrayList<>(change);
			userCoins.clear();
			return new PurchaseResult(PurchaseResultType.CHANGE_CANT_BE_COMPLETED, Optional.empty(), coinReturns);
		}
	}
	
	private void removeChangeCoinsFromStore(List<Coin> change) {
		change.stream().forEach(coin -> coinStore.removeOneQuantity(coin));
	}

	private void mergeUsersCoinsWithCoinStore() {
		userCoins.stream().forEach(coin -> coinStore.addOneQuantity(coin));
	}
	
	private boolean isCoinValid(Coin coin) {
		return acceptableCoins.contains(coin);
	}
	
	private int getBalanceFromUserCoins() {
		int balance = 0;
		for(Coin coin :userCoins) {
			balance += coin.getValue();
		}
		return balance;
	}
	
	private Optional<List<Coin>> calculateChange(int change) {
		List<Coin> changes = new ArrayList<>();
		while(change > 0) {
			boolean hasCoin = false;
			for(Coin coin : acceptableCoins) {
				if(change >= coin.getValue() && coinStore.hasItem(coin)) {
					coinStore.removeOneQuantity(coin);
					changes.add(coin);
					change -= coin.getValue();
					hasCoin = true;
					break;
				}
			}
			if(!hasCoin) {
				revertCoinStore(changes);
				return Optional.empty();
			}
		}
		return Optional.of(changes);
	}
	
	private void revertCoinStore(List<Coin> coins) {
		coins.stream().forEach(coin -> coinStore.addOneQuantity(coin));
	}
}
