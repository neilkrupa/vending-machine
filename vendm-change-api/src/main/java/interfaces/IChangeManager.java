package interfaces;

import enums.CoinType;

import java.util.LinkedHashMap;

import enums.ChanageResponseCode;

public interface IChangeManager {
	/**
	 * This method is used to deposit a coin defined by the input parameter to the
	 * customers credit as well as adding a count for the defined coin to the
	 * vending machines coin map which holds available coins in the machine. This
	 * method then returns the total value that the customer has deposited as a
	 * float.
	 * 
	 * @param coin Coin type to add
	 * @return Total customer credit value
	 */
	public float depositCoin(CoinType coin);

	/**
	 * This method checks whether the customer has enough credit to purchase an item
	 * of a specific price, defined by the input parameter. It further calculates
	 * the change that may be required to be deposited to the customer and checks
	 * whether the vending machine has the correct coins available to deposit said
	 * change. If both of these conditions are met then the customer can purchase
	 * the item.
	 * 
	 * @param price Price of the item to purchase
	 * @return OK if customer can purchase the item, MORE_FUNDS_REQUIRED if the
	 *         customer has not input enough money, CHANGE_UNAVAILABLE if the
	 *         vending machine does not have the correct coins available to dispense
	 *         the required change
	 */
	public ChanageResponseCode checkAvailableFunds(float price);

	/**
	 * This method removes the item price from the customer credit value, calculates
	 * the change required to be given to the customer from the remaining customer
	 * credit, removes the coins from the vending machine coin map, sets the
	 * customer credit value to zero and returns the coins to be dispensed
	 * 
	 * @param itemPrice Price of the item to purchase
	 * @return LinkedHashMap<CoinType, Integer> defining the change that has been
	 *         given
	 */
	public LinkedHashMap<CoinType, Integer> dispenseChange(float itemPrice);

	/**
	 * This method returns the value of the customer credit as a string, reduces the
	 * customer credit to zero and removes the coins from the vending machine coin
	 * map returning the coins to be dispensed
	 * 
	 * @return LinkedHashMap<CoinType, Integer> defining the change that has been
	 *         given
	 */
	public LinkedHashMap<CoinType, Integer> dispenseRefund();

	/**
	 * This method retrieves the value of the change in the coin map as a string
	 * 
	 * @return
	 */
	public String getChangeInMachineAsString();

	/**
	 * This method sets the coin count of a specified coin type in the coin map of
	 * the vending machine
	 * 
	 * @param coinType  Type of coin to set the count for
	 * @param coinCount Count of the coin to set
	 */
	public void updateCoinCount(CoinType coinType, int coinCount);

	/**
	 * This method retrieves the customer credit value stored in the machine rounded
	 * to 2 decimal places
	 * 
	 * @return The customer credit value
	 */
	public float getCustomerCredit();

}
