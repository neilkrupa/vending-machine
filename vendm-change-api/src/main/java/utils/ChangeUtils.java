package utils;

import java.util.LinkedHashMap;

import enums.CoinType;

public class ChangeUtils {
	
	/**
	 * This method produces a string from a coin map
	 * 
	 * @param coinMap Coin map to convert
	 * @return String based on coin map
	 */
	public static String getCoinMapAsString(LinkedHashMap<CoinType, Integer> coinMap) {
		String coinMapString = "";
		for (CoinType coin : coinMap.keySet()) {
			coinMapString += coin + ",[" + coinMap.get(coin) + "],";
		}
		coinMapString += "Change total: " + String.format("%.2f", countChange(coinMap));
		return coinMapString;
	}

	/**
	 * This method counts the values for all coins in a coin map and returns the sum
	 * 
	 * @param coinMap Coin map to count
	 * @return Summation of the values for all coins in the map
	 */
	private static float countChange(LinkedHashMap<CoinType, Integer> coinMap) {
		float totalChange = 0f;
		for (CoinType coin : coinMap.keySet()) {
			totalChange += (coin.getValue() * coinMap.get(coin));
		}
		return ChangeUtils.roundFloatTwoDp(totalChange);
	}

	/**
	 * This method rounds a float to two decimal places and returns it
	 * 
	 * @param value Value to round
	 * @return Rounded value to two decimal places
	 */
	public static float roundFloatTwoDp(float value) {
		return (float) (Math.round((value) * 100.0) / 100.0);
	}

	/**
	 * This method generates a new coin map which contains each item in the
	 * enumeration CoinType as a keys and sets the value for each key to zero
	 * 
	 * @return Empty coin map
	 */
	public static LinkedHashMap<CoinType, Integer> generateEmptyCoinMap() {
		LinkedHashMap<CoinType, Integer> coinMap = new LinkedHashMap<CoinType, Integer>();
		for (CoinType coin : CoinType.values()) {
			coinMap.put(coin, 0);
		}
		return coinMap;
	}
}
