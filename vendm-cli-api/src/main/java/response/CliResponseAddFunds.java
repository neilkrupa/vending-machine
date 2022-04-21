package response;

import enums.CliResponseType;
import enums.CoinType;

public class CliResponseAddFunds extends CliResponse {
	/** Type of coin to add */
	private CoinType coinType;

	/**
	 * Constructor for ADD response message taking in type of coin to add to
	 * customer credit
	 * 
	 * @param reqCoinType Coin type to add to customer credit
	 */
	public CliResponseAddFunds(CoinType reqCoinType) {
		super(CliResponseType.ADD);
		this.coinType = reqCoinType;
	}

	/**
	 * Retrieves the coin type
	 * 
	 * @return The coin type
	 */
	public CoinType getCoinType() {
		return coinType;
	}
}
