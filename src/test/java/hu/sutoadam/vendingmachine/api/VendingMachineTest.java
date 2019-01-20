package hu.sutoadam.vendingmachine.api;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import hu.sutoadam.vendingmachine.factory.VendingMachineFactory;
import hu.sutoadam.vendingmachine.model.Coin;
import hu.sutoadam.vendingmachine.model.Product;
import hu.sutoadam.vendingmachine.model.PurchaseResult;
import hu.sutoadam.vendingmachine.model.PurchaseResultType;

public class VendingMachineTest {

	private static VendingMachine vendingMachine;
	
	
	@Before
	public void setUp() throws Exception {
		vendingMachine = VendingMachineFactory.createVendingMachine();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetProductList() {
		List<Product> expectedProducts = TestUtils.getExpectedProducts();
		
		List<Product> actualProduct = vendingMachine.getProductList();
		
		assertEquals(expectedProducts, actualProduct);
	}

	@Test
	public void testGetAcceptableCoins() {
		List<Coin> expectedCoins = TestUtils.getExpectedCoins();
		
		List<Coin> actualCoins = vendingMachine.getAcceptableCoins();
		
		assertEquals(expectedCoins, actualCoins);
	}

	@Test
	public void testPutCoin_when_validCoinInserted_expected_noCoin() {
		Coin validCoin = TestUtils.getExpectedCoins().get(0);
		Optional<Coin> expectedNoCoin = Optional.empty();
		
		Optional<Coin> noCoin = vendingMachine.putCoin(validCoin);
		
		assertEquals(expectedNoCoin, noCoin);
	}
	
	@Test
	public void testPutCoin_when_invalidCoinInserted_expected_coinReturned() {
		Coin invalidCoin = TestUtils.getInvalidCoin();
		Optional<Coin> expectedInvalidCoin = Optional.of(invalidCoin);
		
		Optional<Coin> actualInvalidCoin = vendingMachine.putCoin(invalidCoin);
		
		assertEquals(expectedInvalidCoin, actualInvalidCoin);
	}

	@Test
	public void testPurchaseProduct_when_exactBalanceAdded_expected_returnProduct() {
		PurchaseResult expectedResult = new PurchaseResult(PurchaseResultType.OK, Optional.of(new Product("Coke", 25)), new ArrayList<>()); 
		
		vendingMachine.putCoin(new Coin("quarter",25));
		PurchaseResult actualResult = vendingMachine.purchaseProduct(new Product("Coke", 25));
		
		assertEquals(expectedResult.getResult(), actualResult.getResult());
		assertEquals(expectedResult.getChange(), actualResult.getChange());
		assertEquals(expectedResult.getProduct(), actualResult.getProduct());
	}
	
	@Test
	public void testPurchaseProduct_when_moreBalanceAdded_expected_returnProductAndChange() {
		Coin quarter = new Coin("quarter", 25);
		List<Coin> changes = new ArrayList<>();
		changes.add(quarter);
		PurchaseResult expectedResult = new PurchaseResult(PurchaseResultType.OK, Optional.of(new Product("Coke", 25)), changes); 
		
		vendingMachine.putCoin(quarter);
		vendingMachine.putCoin(quarter);
		PurchaseResult actualResult = vendingMachine.purchaseProduct(new Product("Coke", 25));
		
		assertEquals(expectedResult.getResult(), actualResult.getResult());
		assertEquals(expectedResult.getChange(), actualResult.getChange());
		assertEquals(expectedResult.getProduct(), actualResult.getProduct());
	}
	
	@Test
	public void testPurchaseProduct_when_noProductCanBeReturned_expected_returnChange() {
		Coin quarter = new Coin("quarter", 25);
		List<Coin> changes = new ArrayList<>();
		changes.add(quarter);
		PurchaseResult expectedResult = new PurchaseResult(PurchaseResultType.PRODUCT_SOLD_OUT, Optional.empty(), changes); 
		
		vendingMachine.putCoin(quarter);
		vendingMachine.purchaseProduct(new Product("Coke", 25));
		
		vendingMachine.putCoin(quarter);
		PurchaseResult actualResult = vendingMachine.purchaseProduct(new Product("Coke", 25));
		
		assertEquals(expectedResult.getResult(), actualResult.getResult());
		assertEquals(expectedResult.getChange(), actualResult.getChange());
		assertEquals(expectedResult.getProduct(), actualResult.getProduct());
	}
	
	@Test
	public void testPurchaseProduct_when_notEnoughBalance_expected_returnState() {
		PurchaseResult expectedResult = new PurchaseResult(PurchaseResultType.INSUFFICIENT_BALANCE, Optional.empty(), new ArrayList<>()); 
		
		Coin dime = new Coin("dime", 10);
		vendingMachine.putCoin(dime);
		PurchaseResult actualResult = vendingMachine.purchaseProduct(new Product("Coke", 25));
		
		assertEquals(expectedResult.getResult(), actualResult.getResult());
		assertEquals(expectedResult.getChange(), actualResult.getChange());
		assertEquals(expectedResult.getProduct(), actualResult.getProduct());
	}
	
	@Test
	public void testPurchaseProduct_when_notEnoughCoinInStash_expected_returnUserCoins() {
		Coin quarter = new Coin("quarter", 25);
		List<Coin> coinList = new ArrayList<>();
		coinList.add(quarter);
		coinList.add(quarter);
		coinList.add(quarter);
		PurchaseResult expectedResult = new PurchaseResult(PurchaseResultType.CHANGE_CANT_BE_COMPLETED, Optional.empty(), coinList); 
		
		vendingMachine.putCoin(quarter);
		vendingMachine.putCoin(quarter);
		vendingMachine.putCoin(quarter);
		PurchaseResult actualResult = vendingMachine.purchaseProduct(new Product("Coke", 25));
		
		assertEquals(expectedResult.getResult(), actualResult.getResult());
		assertEquals(expectedResult.getChange(), actualResult.getChange());
		assertEquals(expectedResult.getProduct(), actualResult.getProduct());
	}

	@Test
	public void testRefund() {
		fail("Not yet implemented");
	}

	@Test
	public void testReset() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetReports() {
		fail("Not yet implemented");
	}

}
