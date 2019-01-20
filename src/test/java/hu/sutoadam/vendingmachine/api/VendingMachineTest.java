package hu.sutoadam.vendingmachine.api;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import hu.sutoadam.vendingmachine.exceptions.MasterKeyWrongException;
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
		Coin validCoin = TestUtils.getQuarterCoin();
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
		Product coke = TestUtils.getCoke();
		PurchaseResult expectedResult = new PurchaseResult(PurchaseResultType.OK, Optional.of(coke), new ArrayList<>()); 
		
		vendingMachine.putCoin(TestUtils.getQuarterCoin());
		PurchaseResult actualResult = vendingMachine.purchaseProduct(coke);
		
		assertEquals(expectedResult.getResult(), actualResult.getResult());
		assertEquals(expectedResult.getChange(), actualResult.getChange());
		assertEquals(expectedResult.getProduct(), actualResult.getProduct());
	}
	
	@Test
	public void testPurchaseProduct_when_moreBalanceAdded_expected_returnProductAndChange() {
		Coin quarter = TestUtils.getQuarterCoin();
		Product coke = TestUtils.getCoke();
		List<Coin> changes = new ArrayList<>();
		changes.add(quarter);
		PurchaseResult expectedResult = new PurchaseResult(PurchaseResultType.OK, Optional.of(coke), changes); 
		
		vendingMachine.putCoin(quarter);
		vendingMachine.putCoin(quarter);
		PurchaseResult actualResult = vendingMachine.purchaseProduct(coke);
		
		assertEquals(expectedResult.getResult(), actualResult.getResult());
		assertEquals(expectedResult.getChange(), actualResult.getChange());
		assertEquals(expectedResult.getProduct(), actualResult.getProduct());
	}
	
	@Test
	public void testPurchaseProduct_when_noProductCanBeReturned_expected_returnChange() {
		Product coke = TestUtils.getCoke();
		Coin quarter = TestUtils.getQuarterCoin();
		List<Coin> changes = new ArrayList<>();
		changes.add(quarter);
		PurchaseResult expectedResult = new PurchaseResult(PurchaseResultType.PRODUCT_SOLD_OUT, Optional.empty(), changes); 
		
		vendingMachine.putCoin(quarter);
		vendingMachine.purchaseProduct(coke);
		vendingMachine.putCoin(quarter);
		PurchaseResult actualResult = vendingMachine.purchaseProduct(coke);
		
		assertEquals(expectedResult.getResult(), actualResult.getResult());
		assertEquals(expectedResult.getChange(), actualResult.getChange());
		assertEquals(expectedResult.getProduct(), actualResult.getProduct());
	}
	
	@Test
	public void testPurchaseProduct_when_notEnoughBalance_expected_returnState() {
		PurchaseResult expectedResult = new PurchaseResult(PurchaseResultType.INSUFFICIENT_BALANCE, Optional.empty(), new ArrayList<>()); 
		
		Coin dime = TestUtils.getDimeCoin();
		vendingMachine.putCoin(dime);
		PurchaseResult actualResult = vendingMachine.purchaseProduct(TestUtils.getCoke());
		
		assertEquals(expectedResult.getResult(), actualResult.getResult());
		assertEquals(expectedResult.getChange(), actualResult.getChange());
		assertEquals(expectedResult.getProduct(), actualResult.getProduct());
	}
	
	@Test
	public void testPurchaseProduct_when_notEnoughCoinInStash_expected_returnUserCoins() {
		Coin quarter = TestUtils.getQuarterCoin();
		List<Coin> coinList = new ArrayList<>();
		coinList.add(quarter);
		coinList.add(quarter);
		coinList.add(quarter);
		PurchaseResult expectedResult = new PurchaseResult(PurchaseResultType.CHANGE_CANT_BE_COMPLETED, Optional.empty(), coinList); 
		
		vendingMachine.putCoin(quarter);
		vendingMachine.putCoin(quarter);
		vendingMachine.putCoin(quarter);
		PurchaseResult actualResult = vendingMachine.purchaseProduct(TestUtils.getCoke());
		
		assertEquals(expectedResult.getResult(), actualResult.getResult());
		assertEquals(expectedResult.getChange(), actualResult.getChange());
		assertEquals(expectedResult.getProduct(), actualResult.getProduct());
	}

	@Test
	public void testRefund() {
		List<Coin> expectedCoins = TestUtils.getExpectedCoins();
		expectedCoins.stream()
				.forEach(coin ->vendingMachine.putCoin(coin));
		
		List<Coin> actualCoins = vendingMachine.refund();
		
		assertEquals(expectedCoins, actualCoins);
	}

	@Test
	public void testReset_when_wrongMasterKeyGranted_expected_exceptionThrown() {
		List<Product> products = TestUtils.getExpectedProducts();
		
		try {
			vendingMachine.reset("wrong", products, 10);
		} catch (MasterKeyWrongException e) {
			assertEquals(e.getMessage(), "Master key is wrong! Please dont try to hack me.");
		}
	}

	@Test
	public void testGetReports() {
		fail("Not yet implemented");
	}

}
