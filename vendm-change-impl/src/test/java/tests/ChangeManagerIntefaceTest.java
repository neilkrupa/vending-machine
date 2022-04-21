package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import core.ChangeManager;
import enums.ChanageResponseCode;
import enums.CoinType;
import interfaces.IChangeManager;

@SuppressWarnings("unchecked")
class ChangeManagerIntefaceTest {

	private Logger LOG = Logger.getLogger(this.getClass().getName());
	private IChangeManager chManager = null;

	@Test
	void initialisation_test() {
		LOG.info("Starting test initialisation_test");

		// Test all specific coin values
		float[] specificCoinInputs = { 2.00f, 1.00f, 0.5f, 0.2f, 0.1f, 0.02f, 0.01f };
		for (float initialChange : specificCoinInputs) {
			try {
				chManager = new ChangeManager(initialChange);
				Field coinMapField = ChangeManager.class.getDeclaredField("coinMap");
				coinMapField.setAccessible(true);
				LinkedHashMap<CoinType, Integer> coinMap = (LinkedHashMap<CoinType, Integer>) coinMapField
						.get(chManager);
				assertTrue(coinMap.get(CoinType.valueOf(initialChange)) == 1);
			} catch (Exception e) {
				LOG.severe(
						"No exception should be thrown when initialising the change manager with standard coin values");
				e.printStackTrace();
				fail();
			}
		}

		// Test min and max values
		float[] minMaxInputs = { Float.MIN_VALUE, Float.MAX_VALUE };
		for (float initialChange : minMaxInputs) {
			try {
				chManager = new ChangeManager(initialChange);
				Field coinMapField = ChangeManager.class.getDeclaredField("coinMap");
				coinMapField.setAccessible(true);
				LinkedHashMap<CoinType, Integer> coinMap = (LinkedHashMap<CoinType, Integer>) coinMapField
						.get(chManager);

				for (CoinType coin : coinMap.keySet()) {
					if (initialChange == Float.MAX_VALUE) {
						if (coin == CoinType.TWO_POUND) {
							assertTrue(coinMap.get(coin) == 2147483647);
						} else {
							assertTrue(coinMap.get(coin) == 0);
						}
					} else {
						assertTrue(coinMap.get(coin) == 0);
					}
				}
			} catch (Exception e) {
				LOG.severe(
						"No exception should be thrown when initialising the change manager with min and max values");
				e.printStackTrace();
				fail();
			}
		}

		// Test float differing sf
		float[] diffSf = { 0.12f, 0.123f, 0.1234f, 0.12345f, 0.123456f, 0.1234567f };
		for (float initialChange : diffSf) {
			try {
				chManager = new ChangeManager(initialChange);
				Field coinMapField = ChangeManager.class.getDeclaredField("coinMap");
				coinMapField.setAccessible(true);
				LinkedHashMap<CoinType, Integer> coinMap = (LinkedHashMap<CoinType, Integer>) coinMapField
						.get(chManager);
				assertTrue(coinMap.get(CoinType.TEN_PENCE) == 1);
				assertTrue(coinMap.get(CoinType.TWO_PENCE) == 1);
			} catch (Exception e) {
				LOG.severe("No exception should be thrown when initialising the change manager with differing sf");
				e.printStackTrace();
				fail();
			}
		}
		LOG.info("Test initialisation_test: OK");
	}

	@Test
	void depositCoin_test() {
		try {
			LOG.info("Starting test depositCoin_test");
			chManager = new ChangeManager(0f);

			Field coinMapField = ChangeManager.class.getDeclaredField("coinMap");
			coinMapField.setAccessible(true);
			LinkedHashMap<CoinType, Integer> coinMap = (LinkedHashMap<CoinType, Integer>) coinMapField.get(chManager);

			for (int coinValue : coinMap.values()) {
				assertTrue(coinValue == 0);
			}

			float currValue = 2.00f;
			assertTrue(chManager.depositCoin(CoinType.TWO_POUND) == currValue);
			assertTrue(coinMap.get(CoinType.TWO_POUND) == 1);

			currValue = 3.00f;
			assertTrue(chManager.depositCoin(CoinType.ONE_POUND) == currValue);
			assertTrue(coinMap.get(CoinType.ONE_POUND) == 1);

			currValue = 3.50f;
			assertTrue(chManager.depositCoin(CoinType.FIFTY_PENCE) == currValue);
			assertTrue(coinMap.get(CoinType.FIFTY_PENCE) == 1);

			currValue = 3.70f;
			assertTrue(chManager.depositCoin(CoinType.TWENTY_PENCE) == currValue);
			assertTrue(coinMap.get(CoinType.TWENTY_PENCE) == 1);

			currValue = 3.80f;
			assertTrue(chManager.depositCoin(CoinType.TEN_PENCE) == currValue);
			assertTrue(coinMap.get(CoinType.TEN_PENCE) == 1);

			currValue = 3.85f;
			assertTrue(chManager.depositCoin(CoinType.FIVE_PENCE) == currValue);
			assertTrue(coinMap.get(CoinType.FIVE_PENCE) == 1);

			currValue = 3.87f;
			assertTrue(chManager.depositCoin(CoinType.TWO_PENCE) == currValue);
			assertTrue(coinMap.get(CoinType.TWO_PENCE) == 1);

			currValue = 3.88f;
			assertTrue(chManager.depositCoin(CoinType.ONE_PENCE) == currValue);
			assertTrue(coinMap.get(CoinType.ONE_PENCE) == 1);

		} catch (Exception e) {
			LOG.severe("No exception should be thrown when depositing a coin");
			e.printStackTrace();
			fail();
		}
		LOG.info("Test depositCoin_test: OK");
	}

	@Test
	void updateCoinCount_test() {
		try {

			// Update specific coin count
			LOG.info("Starting test updateCoinCount_test");
			chManager = new ChangeManager(0);
			Field coinMapField;
			coinMapField = ChangeManager.class.getDeclaredField("coinMap");
			coinMapField.setAccessible(true);
			LinkedHashMap<CoinType, Integer> coinMap = (LinkedHashMap<CoinType, Integer>) coinMapField.get(chManager);
			for (CoinType coin : CoinType.values()) {
				chManager.updateCoinCount(coin, 1);
				assertTrue(coinMap.get(coin) == 1);
				chManager.updateCoinCount(coin, 0);
			}
			
			// Update all coin count 
			chManager.updateCoinCount(null, 100);
			for (CoinType coin : CoinType.values()) {
				assertTrue(coinMap.get(coin) == 100);
			}
			
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			LOG.severe("No exception should be thrown when updating a coin count");
			e.printStackTrace();
			fail();
		}
		LOG.info("Test updateCoinCount_test: OK");
	}

	@Test
	void checkAvailableFunds_test() {
		LOG.info("Starting test checkAvailableFunds_test");
		chManager = new ChangeManager(0);

		assertTrue(chManager
				.checkAvailableFunds(CoinType.ONE_POUND.getValue()) == ChanageResponseCode.MORE_FUNDS_REQUIRED);
		assertTrue(chManager.depositCoin(CoinType.TWO_POUND) == CoinType.TWO_POUND.getValue());
		assertTrue(
				chManager.checkAvailableFunds(CoinType.ONE_POUND.getValue()) == ChanageResponseCode.CHANGE_UNAVAILABLE);
		chManager.updateCoinCount(CoinType.ONE_POUND, 1);
		assertTrue(chManager.checkAvailableFunds(CoinType.ONE_POUND.getValue()) == ChanageResponseCode.OK);
		
		// Check change can be made from two 50p
		resetChangeMap(chManager);
		chManager.updateCoinCount(CoinType.FIFTY_PENCE, 2);
		assertTrue(chManager.checkAvailableFunds(CoinType.ONE_POUND.getValue()) == ChanageResponseCode.OK);

		// Check change can be made from one 50p two 20p and 10p
		resetChangeMap(chManager);
		chManager.updateCoinCount(CoinType.FIFTY_PENCE, 1);
		chManager.updateCoinCount(CoinType.TWENTY_PENCE, 2);
		chManager.updateCoinCount(CoinType.TEN_PENCE, 1);
		assertTrue(chManager.checkAvailableFunds(CoinType.ONE_POUND.getValue()) == ChanageResponseCode.OK);

		// Check change can be made from four 20p two 10p
		resetChangeMap(chManager);
		chManager.updateCoinCount(CoinType.TWENTY_PENCE, 4);
		chManager.updateCoinCount(CoinType.TEN_PENCE, 2);
		assertTrue(chManager.checkAvailableFunds(CoinType.ONE_POUND.getValue()) == ChanageResponseCode.OK);

		// Check change can be made from twenty 5p
		resetChangeMap(chManager);
		chManager.updateCoinCount(CoinType.FIVE_PENCE, 20);
		assertTrue(chManager.checkAvailableFunds(CoinType.ONE_POUND.getValue()) == ChanageResponseCode.OK);

		// Check change can be made from fifty 2p
		resetChangeMap(chManager);
		chManager.updateCoinCount(CoinType.TWO_PENCE, 50);
		assertTrue(chManager.checkAvailableFunds(CoinType.ONE_POUND.getValue()) == ChanageResponseCode.OK);

		// Check change can be made from one hundred 1p
		resetChangeMap(chManager);
		chManager.updateCoinCount(CoinType.ONE_PENCE, 100);
		assertTrue(chManager.checkAvailableFunds(CoinType.ONE_POUND.getValue()) == ChanageResponseCode.OK);
		
		// Check change can be reduced from a two pound coin
		chManager.depositCoin(CoinType.FIFTY_PENCE);
		chManager.depositCoin(CoinType.FIFTY_PENCE);
		chManager.depositCoin(CoinType.FIFTY_PENCE);
		chManager.depositCoin(CoinType.FIFTY_PENCE);
		resetChangeMap(chManager);
		chManager.updateCoinCount(CoinType.ONE_POUND, 3);
		assertTrue(chManager.checkAvailableFunds(CoinType.TWO_POUND.getValue()) == ChanageResponseCode.OK);
		
		// Check change can be reduced from a five pence
		resetChangeMap(chManager);
		chManager.updateCoinCount(CoinType.TWO_PENCE, 2);
		chManager.updateCoinCount(CoinType.ONE_PENCE, 1);
		assertTrue(chManager.checkAvailableFunds(3.95f) == ChanageResponseCode.OK);

		LOG.info("Test checkAvailableFunds_test: OK");
	}

	@Test
	void dispenseChange_test() {
		LOG.info("Starting test dispenseChange_test");
		try {
			chManager = new ChangeManager(0);

			Field coinMapField;

			coinMapField = ChangeManager.class.getDeclaredField("coinMap");
			coinMapField.setAccessible(true);
			LinkedHashMap<CoinType, Integer> coinMap = (LinkedHashMap<CoinType, Integer>) coinMapField.get(chManager);

			/*
			 * This method removes the item price from the customer credit value, calculates
			 * the change required to be given to the customer from the remaining customer
			 * credit, removes the coins from the vending machine coin map, sets the
			 * customer credit value to zero and returning the coins to be dispensed
			 */

			LinkedHashMap<CoinType, Integer> returnedValue = chManager.dispenseChange(CoinType.ONE_POUND.getValue());
			for (Integer coinCoint : returnedValue.values()) {
				assertTrue(coinCoint == 0);
			}

			chManager.updateCoinCount(CoinType.TWO_POUND, 1);
			chManager.updateCoinCount(CoinType.ONE_POUND, 1);
			chManager.updateCoinCount(CoinType.FIFTY_PENCE, 2);
			chManager.updateCoinCount(CoinType.TWENTY_PENCE, 5);
			chManager.updateCoinCount(CoinType.TEN_PENCE, 10);
			chManager.updateCoinCount(CoinType.FIVE_PENCE, 20);
			chManager.updateCoinCount(CoinType.TWO_PENCE, 50);
			chManager.updateCoinCount(CoinType.ONE_PENCE, 100);

			// Add TWO_POUND customer credit
			chManager.depositCoin(CoinType.TWO_POUND);
			// Check correct change dispensed for item value ONE_POUND
			returnedValue = chManager.dispenseChange(CoinType.ONE_POUND.getValue());
			// Should dispense ONE_POUND coin
			for (CoinType coin : returnedValue.keySet()) {
				if (coin == CoinType.ONE_POUND) {
					assertTrue(returnedValue.get(coin) == 1);
				} else {
					assertTrue(returnedValue.get(coin) == 0);
				}
			}
			// Check that the change in the coin map has been decreased by the correct
			// amount
			assertTrue(coinMap.get(CoinType.ONE_POUND) == 0);
			assertTrue(chManager.getCustomerCredit() == 0);
			
			// ------
			
			// Add TWO_POUND customer credit
			chManager.depositCoin(CoinType.TWO_POUND);
			// Check correct change dispensed for item value ONE_POUND
			returnedValue = chManager.dispenseChange(CoinType.ONE_POUND.getValue());
			// Should dispense FIFTY_PENCE coins
			for (CoinType coin : returnedValue.keySet()) {
				if (coin == CoinType.FIFTY_PENCE) {
					assertTrue(returnedValue.get(coin) == 2);
				} else {
					assertTrue(returnedValue.get(coin) == 0);
				}
			}
			// Check that the change in the coin map has been decreased by the correct
			// amount
			assertTrue(coinMap.get(CoinType.FIFTY_PENCE) == 0);
			assertTrue(chManager.getCustomerCredit() == 0);
			
			// ------
			// Add TWO_POUND customer credit
			chManager.depositCoin(CoinType.TWO_POUND);
			// Check correct change dispensed for item value ONE_POUND
			returnedValue = chManager.dispenseChange(CoinType.ONE_POUND.getValue());
			// Should dispense TWENTY_PENCE coins
			for (CoinType coin : returnedValue.keySet()) {
				if (coin == CoinType.TWENTY_PENCE) {
					assertTrue(returnedValue.get(coin) == 5);
				} else {
					assertTrue(returnedValue.get(coin) == 0);
				}
			}
			// Check that the change in the coin map has been decreased by the correct
			// amount
			assertTrue(coinMap.get(CoinType.TWENTY_PENCE) == 0);
			assertTrue(chManager.getCustomerCredit() == 0);
			
			// ------
			
			// Add TWO_POUND customer credit
			chManager.depositCoin(CoinType.TWO_POUND);
			// Check correct change dispensed for item value ONE_POUND
			returnedValue = chManager.dispenseChange(CoinType.ONE_POUND.getValue());
			// Should dispense TEN_PENCE coins
			for (CoinType coin : returnedValue.keySet()) {
				if (coin == CoinType.TEN_PENCE) {
					assertTrue(returnedValue.get(coin) == 10);
				} else {
					assertTrue(returnedValue.get(coin) == 0);
				}
			}
			// Check that the change in the coin map has been decreased by the correct
			// amount
			assertTrue(coinMap.get(CoinType.TEN_PENCE) == 0);
			assertTrue(chManager.getCustomerCredit() == 0);
			
			// ------
			
			// Add TWO_POUND customer credit
			chManager.depositCoin(CoinType.TWO_POUND);
			// Check correct change dispensed for item value ONE_POUND
			returnedValue = chManager.dispenseChange(CoinType.ONE_POUND.getValue());
			// Should dispense FIVE_PENCE coins
			for (CoinType coin : returnedValue.keySet()) {
				if (coin == CoinType.FIVE_PENCE) {
					assertTrue(returnedValue.get(coin) == 20);
				} else {
					assertTrue(returnedValue.get(coin) == 0);
				}
			}
			// Check that the change in the coin map has been decreased by the correct
			// amount
			assertTrue(coinMap.get(CoinType.FIVE_PENCE) == 0);
			assertTrue(chManager.getCustomerCredit() == 0);
			
			// ------
			
			// Add TWO_POUND customer credit
			chManager.depositCoin(CoinType.TWO_POUND);
			// Check correct change dispensed for item value ONE_POUND
			returnedValue = chManager.dispenseChange(CoinType.ONE_POUND.getValue());
			// Should dispense TWO_PENCE coins
			for (CoinType coin : returnedValue.keySet()) {
				if (coin == CoinType.TWO_PENCE) {
					assertTrue(returnedValue.get(coin) == 50);
				} else {
					assertTrue(returnedValue.get(coin) == 0);
				}
			}
			// Check that the change in the coin map has been decreased by the correct
			// amount
			assertTrue(coinMap.get(CoinType.TWO_PENCE) == 0);
			assertTrue(chManager.getCustomerCredit() == 0);
			
			// ------
			
			// Add TWO_POUND customer credit
			chManager.depositCoin(CoinType.TWO_POUND);
			// Check correct change dispensed for item value ONE_POUND
			returnedValue = chManager.dispenseChange(CoinType.ONE_POUND.getValue());
			// Should dispense ONE_PENCE coins
			for (CoinType coin : returnedValue.keySet()) {
				if (coin == CoinType.ONE_PENCE) {
					assertTrue(returnedValue.get(coin) == 100);
				} else {
					assertTrue(returnedValue.get(coin) == 0);
				}
			}
			// Check that the change in the coin map has been decreased by the correct
			// amount
			assertTrue(coinMap.get(CoinType.ONE_PENCE) == 0);
			assertTrue(chManager.getCustomerCredit() == 0);
			
			// Bad result test
			chManager.depositCoin(CoinType.TWO_POUND);
			returnedValue = chManager.dispenseChange(CoinType.ONE_POUND.getValue());
			for (Integer coinCount : returnedValue.values()) {
				assertTrue(coinCount==0);
			}
			
			
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			LOG.severe("No exception should be thrown when dispensing change");
			e.printStackTrace();
			fail();
		}
		LOG.info("Test dispenseChange_test: OK");
	}

	@Test
	void dispenseRefund_test() {
		LOG.info("Starting test dispenseRefund_test");
		chManager = new ChangeManager(0);

		// Check nothing returned if no customer credit
		LinkedHashMap<CoinType, Integer> returnedValue = chManager.dispenseRefund();
		for (Integer coinCoint : returnedValue.values()) {
			assertTrue(coinCoint == 0);
		}

		// Check for each coin type inserted
		for (CoinType coin : CoinType.values()) {
			chManager.depositCoin(coin);
			returnedValue = chManager.dispenseRefund();
			for (CoinType returnedCoin : returnedValue.keySet()) {
				if (returnedCoin == coin) {
					assertTrue(returnedValue.get(returnedCoin) == 1);
				} else {
					assertTrue(returnedValue.get(returnedCoin) == 0);
				}
			}
		}

		// Bad case test
		chManager.depositCoin(CoinType.ONE_POUND);
		chManager.updateCoinCount(CoinType.ONE_POUND, 0);
		returnedValue = chManager.dispenseRefund();
		for (int returnedCoinValue : returnedValue.values()) {
			assertTrue(returnedCoinValue == 0);
		}

		LOG.info("Test dispenseRefund_test: OK");
	}

	@Test
	void getChangeInMachineAsString_test() {
		LOG.info("Starting test getChangeInMachineAsString_test");

		chManager = new ChangeManager(0);

		String returnedString = chManager.getChangeInMachineAsString();
		assertTrue(returnedString.equals("TWO_POUND,[0],ONE_POUND,[0],FIFTY_PENCE,[0],TWENTY_PENCE,"
				+ "[0],TEN_PENCE,[0],FIVE_PENCE,[0],TWO_PENCE,[0],ONE_PENCE," + "[0],Change total: 0.00"));

		chManager.updateCoinCount(CoinType.TWO_POUND, 1);
		returnedString = chManager.getChangeInMachineAsString();
		assertTrue(returnedString.equals("TWO_POUND,[1],ONE_POUND,[0],FIFTY_PENCE,[0],TWENTY_PENCE,"
				+ "[0],TEN_PENCE,[0],FIVE_PENCE,[0],TWO_PENCE,[0],ONE_PENCE," + "[0],Change total: 2.00"));

		chManager.updateCoinCount(CoinType.ONE_POUND, 1);
		returnedString = chManager.getChangeInMachineAsString();
		assertTrue(returnedString.equals("TWO_POUND,[1],ONE_POUND,[1],FIFTY_PENCE,[0],TWENTY_PENCE,"
				+ "[0],TEN_PENCE,[0],FIVE_PENCE,[0],TWO_PENCE,[0],ONE_PENCE," + "[0],Change total: 3.00"));

		chManager.updateCoinCount(CoinType.FIFTY_PENCE, 1);
		returnedString = chManager.getChangeInMachineAsString();
		assertTrue(returnedString.equals("TWO_POUND,[1],ONE_POUND,[1],FIFTY_PENCE,[1],TWENTY_PENCE,"
				+ "[0],TEN_PENCE,[0],FIVE_PENCE,[0],TWO_PENCE,[0],ONE_PENCE," + "[0],Change total: 3.50"));

		chManager.updateCoinCount(CoinType.TWENTY_PENCE, 1);
		returnedString = chManager.getChangeInMachineAsString();
		assertTrue(returnedString.equals("TWO_POUND,[1],ONE_POUND,[1],FIFTY_PENCE,[1],TWENTY_PENCE,"
				+ "[1],TEN_PENCE,[0],FIVE_PENCE,[0],TWO_PENCE,[0],ONE_PENCE," + "[0],Change total: 3.70"));

		chManager.updateCoinCount(CoinType.TEN_PENCE, 1);
		returnedString = chManager.getChangeInMachineAsString();
		assertTrue(returnedString.equals("TWO_POUND,[1],ONE_POUND,[1],FIFTY_PENCE,[1],TWENTY_PENCE,"
				+ "[1],TEN_PENCE,[1],FIVE_PENCE,[0],TWO_PENCE,[0],ONE_PENCE," + "[0],Change total: 3.80"));

		chManager.updateCoinCount(CoinType.FIVE_PENCE, 1);
		returnedString = chManager.getChangeInMachineAsString();
		assertTrue(returnedString.equals("TWO_POUND,[1],ONE_POUND,[1],FIFTY_PENCE,[1],TWENTY_PENCE,"
				+ "[1],TEN_PENCE,[1],FIVE_PENCE,[1],TWO_PENCE,[0],ONE_PENCE," + "[0],Change total: 3.85"));

		chManager.updateCoinCount(CoinType.TWO_PENCE, 1);
		returnedString = chManager.getChangeInMachineAsString();
		assertTrue(returnedString.equals("TWO_POUND,[1],ONE_POUND,[1],FIFTY_PENCE,[1],TWENTY_PENCE,"
				+ "[1],TEN_PENCE,[1],FIVE_PENCE,[1],TWO_PENCE,[1],ONE_PENCE," + "[0],Change total: 3.87"));

		chManager.updateCoinCount(CoinType.ONE_PENCE, 1);
		returnedString = chManager.getChangeInMachineAsString();
		assertTrue(returnedString.equals("TWO_POUND,[1],ONE_POUND,[1],FIFTY_PENCE,[1],TWENTY_PENCE,"
				+ "[1],TEN_PENCE,[1],FIVE_PENCE,[1],TWO_PENCE,[1],ONE_PENCE," + "[1],Change total: 3.88"));

		LOG.info("Test getChangeInMachineAsString_test: OK");
	}

	@Test
	void getCustomerCredit_test() {
		LOG.info("Starting test getCustomerCredit_test");
		chManager = new ChangeManager(0);

		float currValue = 0.00f;
		assertTrue(chManager.getCustomerCredit() == currValue);

		currValue = 2.00f;
		chManager.depositCoin(CoinType.TWO_POUND);
		assertTrue(chManager.getCustomerCredit() == currValue);

		currValue = 3.00f;
		chManager.depositCoin(CoinType.ONE_POUND);
		assertTrue(chManager.getCustomerCredit() == currValue);

		currValue = 3.50f;
		chManager.depositCoin(CoinType.FIFTY_PENCE);
		assertTrue(chManager.getCustomerCredit() == currValue);

		currValue = 3.70f;
		chManager.depositCoin(CoinType.TWENTY_PENCE);
		assertTrue(chManager.getCustomerCredit() == currValue);

		currValue = 3.80f;
		chManager.depositCoin(CoinType.TEN_PENCE);
		assertTrue(chManager.getCustomerCredit() == currValue);

		currValue = 3.85f;
		chManager.depositCoin(CoinType.FIVE_PENCE);
		assertTrue(chManager.getCustomerCredit() == currValue);

		currValue = 3.87f;
		chManager.depositCoin(CoinType.TWO_PENCE);
		assertTrue(chManager.getCustomerCredit() == currValue);

		currValue = 3.88f;
		chManager.depositCoin(CoinType.ONE_PENCE);
		assertTrue(chManager.getCustomerCredit() == currValue);

		LOG.info("Test getCustomerCredit_test: OK");
	}
	
	private void resetChangeMap(IChangeManager chManager)
	{
		chManager.updateCoinCount(CoinType.TWO_POUND, 0);
		chManager.updateCoinCount(CoinType.ONE_POUND, 0);
		chManager.updateCoinCount(CoinType.FIFTY_PENCE, 0);
		chManager.updateCoinCount(CoinType.TWENTY_PENCE, 0);
		chManager.updateCoinCount(CoinType.TEN_PENCE, 0);
		chManager.updateCoinCount(CoinType.FIVE_PENCE, 0);
		chManager.updateCoinCount(CoinType.TWO_PENCE, 0);
		chManager.updateCoinCount(CoinType.ONE_PENCE, 0);
	}
}
