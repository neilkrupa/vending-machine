package enums;

/**
 * This enumeration defines the coins that are available for use within the
 * vending machine alongside the float value of each coin
 * 
 * @author neilk
 *
 */
public enum CoinType {
	TWO_POUND(2f), 
	ONE_POUND(1f), 
	FIFTY_PENCE(0.5f), 
	TWENTY_PENCE(0.2f), 
	TEN_PENCE(0.1f), 
	FIVE_PENCE(0.05f),
	TWO_PENCE(0.02f), 
	ONE_PENCE(0.01f);

	/** Float value of the coin */
	private float value;

	/**
	 * Constructor taking in float value of the coin
	 * @param floatValue Float value of the coin
	 */
	private CoinType(float floatValue) {
		this.value = floatValue;
	}

	/**
	 * Retrieves the float value of the coin
	 * @return The float value of the coin
	 */
	public float getValue() {
		return value;
	}

	/**
	 * Retrieves the coin type enumeration based on its value
	 * @param initialChange Float value of the enumeration
	 * @return The associated coin type enumeration based on its value
	 * @throws Exception Exception thrown if no coin type found
	 */
	public static CoinType valueOf(float value) throws Exception 
	{
		for(CoinType coin : CoinType.values())
		{
			if(coin.getValue() == value)
			{
				return coin;
			}
		}
		throw new Exception("Coin value does not match any coin type value");
	}
}
