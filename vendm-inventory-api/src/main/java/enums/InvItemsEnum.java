package enums;

/**
 * This enumeration defines the items to be added to the vending machine
 * inventory
 * 
 * @author neilk
 *
 */
public enum InvItemsEnum {
	QUAVERS(3, 1.10f), 
	CHEESE_AND_ONION(3, 2.20f), 
	ROAST_CHICKEN(15, 3.30f), 
	PRAWN_COCKTAIL(0, 4.40f), 
	WHEAT_CRUNCHIES(3, 1.10f),
	NIK_NAKS(3, 12.10f), 
	READY_SALTED(3, 1.00f), 
	SALT_AND_VIGENAR(3, 1.50f), 
	SMOKEY_BACON(3, 1.10f), 
	WOTSITS(3, 1.10f);

	/** Number of this item in the vending machine */
	private int count;
	
	/** Price of the item */
	private float price;

	/**
	 * Constructor
	 * @param count Number of this item in the vending machine
	 * @param price Price of the item
	 */
	InvItemsEnum(int count, float price) {
		this.count = count;
		this.price = price;
	}

	/**
	 * Retrieves the number of this item in the vending machine
	 * @return The number of this item in the vending machine
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Retrieves the price of the item
	 * @return The price of the item
	 */
	public float getPrice() {
		return price;
	}
}
