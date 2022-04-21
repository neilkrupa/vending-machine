package interfaces;

import enums.InvResponseCode;

public interface IInventoryManager {
	/**
	 * This method checks whether the item is in stock and returns a corresponding
	 * response code
	 * 
	 * @param itemCode Item code of the item to check
	 * @return OK if item exists and is in stock, ITEM_DOESNT_EXIST if item code is
	 *         not in the inventory map, ITEM_SOLD_OUT if the item count is zero
	 */
	public InvResponseCode itemInStock(String itemCode);

	/**
	 * This method returns the price of a specific item in the inventory
	 * 
	 * @param itemCode Code of the item to check
	 * @return The price of the item as a float
	 */
	public float getItemPrice(String itemCode);

	/**
	 * This method decreases the count of the item in the inventory and returns a
	 * string outlining the item that has been dispensed
	 * 
	 * @param itemCode Item code to decrease the count of
	 * @return String defining the name of the item being dispensed
	 */
	public String dispenseItem(String itemCode);

	/**
	 * This method builds and returns a comma separated string of all items defined
	 * in the inventory
	 * 
	 * @return A comma separated string of all items defined in the inventory
	 */
	public String getDisplayDataInventoryItems();
}
