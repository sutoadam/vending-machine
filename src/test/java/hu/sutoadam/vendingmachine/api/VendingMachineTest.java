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
	public void testPutCoin_when_validCoinInserted() {
		Coin validCoin = TestUtils.getExpectedCoins().get(0);
		Optional<Coin> expectedNoCoin = Optional.empty();
		
		Optional<Coin> noCoin = vendingMachine.putCoin(validCoin);
		
		assertEquals(expectedNoCoin, noCoin);
	}
	
	@Test
	public void testPutCoin_when_invalidCoinInserted() {
		Coin invalidCoin = TestUtils.getInvalidCoin();
		Optional<Coin> expectedInvalidCoin = Optional.of(invalidCoin);
		
		Optional<Coin> actualInvalidCoin = vendingMachine.putCoin(invalidCoin);
		
		assertEquals(expectedInvalidCoin, actualInvalidCoin);
	}

	@Test
	public void testPurchaseProduct() {
		fail("Not yet implemented");
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
