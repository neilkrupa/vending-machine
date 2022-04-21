package core;

import java.util.LinkedHashMap;

import enums.CoinType;
import exceptions.ChangeCalculationException;
import enums.ChanageResponseCode;
import interfaces.IChangeManager;
import processing.ChangeProcessor;
import utils.ChangeUtils;

public class ChangeManager implements IChangeManager {
	//
	/**
	 * Count of each coin held by the vending machine (Linked hashmap used to
	 * preserve insertion order)
	 */
	private static LinkedHashMap<CoinType, Integer> coinMap;

	/**
	 * Value of the coins added by the user
	 */
	private float customerCredit = 0f;

	/**
	 * Constructor initialises the class using the initial change float
	 * 
	 * @param initialChange The value used to populate the coin map during
	 *                      initialisation
	 */
	public ChangeManager(float initialChange) {
		initialise(initialChange);
	}

	/**
	 * This method initiaises the coin map by creating an entry for each coin
	 * defined in the CoinType enumeration and sets each coins count to zero. It
	 * then populates the coin map count values using the change processor and a
	 * passed in initial change float. This populates the coin map to the lowest
	 * number of coins that can be made up for the passed in float. These values can
	 * be modified after initialisation.
	 * 
	 * @param initialChange The value used to populate the coin map
	 */
	public void initialise(float initialChange) {
		ChangeManager.coinMap = ChangeUtils.generateEmptyCoinMap();
		ChangeProcessor.populateCoinMapFromFloat(coinMap, initialChange);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateCoinCount(CoinType coinType, int coinCount) {
		if(coinType != null)
		{
			coinMap.put(coinType, coinCount);
		}
		else
		{
			for(CoinType coin : coinMap.keySet())
			{
				coinMap.put(coin, coinCount);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float depositCoin(CoinType coin) {
		this.customerCredit += coin.getValue();
		ChangeManager.coinMap.put(coin, (ChangeManager.coinMap.get(coin) + 1));
		return ChangeUtils.roundFloatTwoDp(this.customerCredit);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LinkedHashMap<CoinType, Integer> dispenseChange(float itemPrice) {
		try {
			if (!((this.customerCredit - itemPrice) < 0)) {
				LinkedHashMap<CoinType, Integer> customerChangeCoinMap = ChangeUtils.generateEmptyCoinMap();
				this.customerCredit -= itemPrice;
				customerChangeCoinMap = ChangeProcessor.calcCoinsFromFloat(coinMap, this.customerCredit);
				removeCoinsFromMachine(customerChangeCoinMap);
				this.customerCredit = 0;
				return customerChangeCoinMap;
			} else {
				// Defensive coding: This should never get called as the change is checked
				// previously
				return ChangeUtils.generateEmptyCoinMap();
			}
		} catch (ChangeCalculationException e) {
			// Defensive coding: This should never get called as the change is checked
			// previously
			return ChangeUtils.generateEmptyCoinMap();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LinkedHashMap<CoinType, Integer> dispenseRefund() {
		try {
			LinkedHashMap<CoinType, Integer> customerChangeCoinMap = ChangeUtils.generateEmptyCoinMap();
			customerChangeCoinMap = ChangeProcessor.calcCoinsFromFloat(coinMap, this.customerCredit);
			removeCoinsFromMachine(customerChangeCoinMap);
			this.customerCredit = 0;
			return customerChangeCoinMap;
		} catch (ChangeCalculationException e) {
			// This should never get called as the user has entered all of their change
			return ChangeUtils.generateEmptyCoinMap();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChanageResponseCode checkAvailableFunds(float price) {
		if (this.customerCredit < price) {
			return ChanageResponseCode.MORE_FUNDS_REQUIRED;
		} else if (!checkEnoughChangeInMachine(price)) {
			return ChanageResponseCode.CHANGE_UNAVAILABLE;
		} else {
			return ChanageResponseCode.OK;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getChangeInMachineAsString() {
		return ChangeUtils.getCoinMapAsString(coinMap);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getCustomerCredit() {
		return ChangeUtils.roundFloatTwoDp(this.customerCredit);
	}

	/**
	 * This method ensures that the change required can be made up from the coins
	 * within the machine
	 * 
	 * @param price Price of the item to be purchased
	 * @return True if enough change in machine, false if not
	 */
	private boolean checkEnoughChangeInMachine(float price) {
		float changeRequired = ChangeUtils.roundFloatTwoDp(this.customerCredit - price);
		try {
			ChangeProcessor.calcCoinsFromFloat(coinMap, changeRequired);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Removes coins from the coin change map based on a passed in coin map
	 * 
	 * @param customerChangeCoinMap Coin map containing coins to be removed from the
	 *                              vending machine
	 */
	private void removeCoinsFromMachine(LinkedHashMap<CoinType, Integer> customerChangeCoinMap) {
		for (CoinType coin : customerChangeCoinMap.keySet()) {
			if (customerChangeCoinMap.get(coin) > 0) {
				int changeCoinCount = ChangeManager.coinMap.get(coin);
				changeCoinCount -= customerChangeCoinMap.get(coin);
				ChangeManager.coinMap.put(coin, changeCoinCount);
			}
		}
	}

}
