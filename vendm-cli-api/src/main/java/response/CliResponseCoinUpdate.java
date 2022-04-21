package response;

import enums.CliResponseType;
import enums.CoinType;

public class CliResponseCoinUpdate extends CliResponse {

	/** Coin type to set count for */
	private CoinType coinType = null;

	/** Whether all coins are to be set to a count */
	private boolean allCoins = false;

	/** Count to set */
	private int count;

	/**
	 * Constructor defining a INIT_COIN response type taking in coin type and count
	 * for that coin
	 * 
	 * @param resCoinType Coin type to set count for
	 * @param resCount    Count to set
	 */
	public CliResponseCoinUpdate(CoinType resCoinType, int resCount) {
		super(CliResponseType.INIT_COIN);
		this.coinType = resCoinType;
		this.count = resCount;
	}

	/**
	 * Constructor defining a INIT_COIN response type taking in a count to set all
	 * coins to
	 * 
	 * @param resCount Count to set all couns to
	 */
	public CliResponseCoinUpdate(int resCount) {
		super(CliResponseType.INIT_COIN);
		this.count = resCount;
		this.allCoins = true;
	}

	/**
	 * Retrieves the coin type
	 * 
	 * @return Coin type
	 */
	public CoinType getCoinType() {
		return coinType;
	}

	/**
	 * Retrieves the coin count
	 * 
	 * @return The coin count
	 */
	public int getCoinCount() {
		return count;
	}

	/**
	 * Retrieve whether all coins are to be set to a specified count
	 * 
	 * @return
	 */
	public boolean getAllCoins() {
		return this.allCoins;
	}
}
