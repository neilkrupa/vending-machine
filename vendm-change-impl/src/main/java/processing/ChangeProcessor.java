/**
 * 
 */
package processing;

import java.util.LinkedHashMap;
import enums.CoinType;
import exceptions.ChangeCalculationException;
import utils.ChangeUtils;

/**
 * This class handles the processing of change based on coin maps and float
 * values
 * 
 * @author neilk
 *
 */
public class ChangeProcessor {

	/**
	 * This method populates a coin map with the smallest number of coins required
	 * to make up a float value
	 * 
	 * @param coinMap          Map to populate
	 * @param totalChangeValue Float value for the summation of coins required
	 */
	public static void populateCoinMapFromFloat(LinkedHashMap<CoinType, Integer> coinMap, float totalChangeValue) {
		LinkedHashMap<CoinType, Integer> populatedCoinMap = coinMap;
		float currentTotal = totalChangeValue;
		for (CoinType coin : coinMap.keySet()) {
			currentTotal = calcCoinCountFromFloat(populatedCoinMap, coin, currentTotal);
		}
		coinMap = populatedCoinMap;
	}

	/**
	 * This method calculates the number of coins of a specific type that can be
	 * extracted from a float
	 * 
	 * @param coinMap      Coin map to populate
	 * @param type         Type of coin to extract from the float
	 * @param currentTotal Float to process
	 * @return The remainder of the passed in float after coins of a specific type
	 *         have been extracted
	 */
	private static float calcCoinCountFromFloat(LinkedHashMap<CoinType, Integer> coinMap, CoinType type,
			float currentTotal) {
		float remainder = ChangeUtils.roundFloatTwoDp(currentTotal % type.getValue());
		float amount = ChangeUtils.roundFloatTwoDp(currentTotal - remainder);
		int count = (int) ChangeUtils.roundFloatTwoDp(amount / type.getValue());
		coinMap.put(type, count);
		return remainder;
	}

	/**
	 * This method calculates a coin map which contains the smallest number of coins
	 * from a defined float based on the coins available in a passed in coins map.
	 * If the coins are not available to create the value of the float then an
	 * exception is thrown.
	 * 
	 * @param storedCoinMap Coin map containing available coins
	 * @param value         Value of the summation of the created coin map
	 * @return Coin map containing the smallest number of available coins summating
	 *         to the value of the float passed in
	 * @throws ChangeCalculationException Exception thrown if value cant be made
	 *                                    from available coins
	 */
	@SuppressWarnings("unchecked")
	public static LinkedHashMap<CoinType, Integer> calcCoinsFromFloat(LinkedHashMap<CoinType, Integer> storedCoinMap,
			float value) throws ChangeCalculationException {
		// Calculate the minimum number of coins required
		LinkedHashMap<CoinType, Integer> minCoinMap = ChangeUtils.generateEmptyCoinMap();
		LinkedHashMap<CoinType, Integer> availableCoinMap = (LinkedHashMap<CoinType, Integer>) storedCoinMap.clone();
		populateCoinMapFromFloat(minCoinMap, value);

		// Check which coins the machine currently has and calculate remaining coins
		// required
		for (CoinType minCoin : minCoinMap.keySet()) {
			int availableChangeCoinCount = availableCoinMap.get(minCoin);
			int minChangeCoinCount = minCoinMap.get(minCoin);
			// While both coin counts is more than zero reduce the value to find remaining
			// min change required
			while (availableChangeCoinCount > 0 && minChangeCoinCount > 0) {
				minChangeCoinCount--;
				availableChangeCoinCount--;
				availableCoinMap.put(minCoin, availableChangeCoinCount);
			}
			if (minChangeCoinCount > 0) {
				reduceCoin(minCoin, minCoinMap, minChangeCoinCount, availableCoinMap);
			}
		}
		return minCoinMap;
	}

	/**
	 * This method reduces a coin into smaller coins and updates the passed in map
	 * to represent the change. This process is repeated for the count required.
	 * 
	 * @param coin               Type of coin to reduce
	 * @param minCoinMap         Coin map to update
	 * @param minChangeCoinCount Number of times to repeat this process
	 * @param availableCoinMap   Coins available for change
	 * @throws ChangeCalculationException Exception thrown if coin cannot be reduced
	 *                                    any further
	 */
	private static void reduceCoin(CoinType coin, LinkedHashMap<CoinType, Integer> minCoinMap, int minChangeCoinCount,
			LinkedHashMap<CoinType, Integer> availableCoinMap) throws ChangeCalculationException {
		// Reduce coin into smaller coins and update the map to represent this change
		for (int iterations = 0; iterations < minChangeCoinCount; iterations++) {

			// Remove the coin to be reduced from the map
			int coinCount = minCoinMap.get(coin);
			coinCount--;
			minCoinMap.put(coin, coinCount);

			switch (coin) {
			case TWO_POUND:
				// Reduce two pound coin to two one pound coins
				reduceTo(CoinType.ONE_POUND, 2, minCoinMap);
				break;
			case ONE_POUND:
				// This reduction depends on the lowest coins stored in the map
				// - if a 50p is available then this should be reduced to 50p
				// - if only 20p is available then the coin should be reduced to 20p
				if (availableCoinMap.get(CoinType.FIFTY_PENCE) > 0) {
					// Reduce one pound coin to two 50 pence coins
					reduceTo(CoinType.FIFTY_PENCE, 2, minCoinMap);
				} else {
					// Reduce one pound coin to five 20 pence coins
					reduceTo(CoinType.TWENTY_PENCE, 5, minCoinMap);
					break;
				}
				break;
			case FIFTY_PENCE:
				// Reduce 50 pence coin to two 20 pence coins and a 10 pence coin
				reduceTo(CoinType.TEN_PENCE, 1, minCoinMap);
				reduceTo(CoinType.TWENTY_PENCE, 2, minCoinMap);
				break;
			case TWENTY_PENCE:
				// Reduce 20 pence coin to two 10 pence coins
				reduceTo(CoinType.TEN_PENCE, 2, minCoinMap);
				break;
			case TEN_PENCE:
				// This reduction depends on the lowest coins stored in the map
				// - if a 5p is available then this should be reduced to 5p
				// - if only 2p is available then the coin should be reduced to 2p
				if (availableCoinMap.get(CoinType.FIVE_PENCE) > 0) {
					// Reduce 10 pence coin to two 5 pence coins
					reduceTo(CoinType.FIVE_PENCE, 2, minCoinMap);
					break;
				} else {
					// Reduce 10 pence coin to five 2 pence coins
					reduceTo(CoinType.TWO_PENCE, 5, minCoinMap);
					break;
				}
			case FIVE_PENCE:
				// Reduce 5 pence coin to two 2 pence coins and one 1 pence coin
				reduceTo(CoinType.TWO_PENCE, 2, minCoinMap);
				reduceTo(CoinType.ONE_PENCE, 1, minCoinMap);
				break;
			case TWO_PENCE:
				// Reduce 2 pence coin to two 1 pence coins
				reduceTo(CoinType.ONE_PENCE, 2, minCoinMap);
				break;
			case ONE_PENCE:
			default:
				// One pence and default cannot be broken down so throw exception
				throw new ChangeCalculationException("Not enough change in machine for this transaction");
			}
		}
	}

	/**
	 * This method adds a specified number of coins to the minCoinMap
	 * 
	 * @param coin         Coin type to add
	 * @param coinAddCount Number of coins to add
	 * @param minCoinMap   Coin map to add the coins to
	 */
	private static void reduceTo(CoinType coin, int coinAddCount, LinkedHashMap<CoinType, Integer> minCoinMap) {
		int minCoinCount = 0;
		minCoinCount = minCoinMap.get(coin);
		minCoinCount += coinAddCount;
		minCoinMap.put(coin, minCoinCount);
	}
}
