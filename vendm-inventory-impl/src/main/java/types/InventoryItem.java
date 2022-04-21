/**
 * The inventory package defines the classes for the vending machine inventory
 */
package types;

/**
 * This class defines an item that can be stored in the vending machine inventory
 * @author neilk
 *
 */
public class InventoryItem 
{
	/** Name of item */
	private String name;
	
	/** Count of item in inventory */
	private int count;
	
	/** Price of item */
	private float price;
	
	/**
	 * Constructor for inventory item containing name and price of the item
	 * @param itemName Name of the inventory item
	 * @param itemCode Inventory code of the inventory item
	 * @param itemPrice Price of the inventory item
	 */
	public InventoryItem(String itemName, int itemCount, float itemPrice)
	{
		this.name = itemName;
		this.count = itemCount;
		this.price = itemPrice;
	}
	
	/**
	 * Retrieves the name of the inventory item
	 * @return The name of the inventory item
	 */
	public String getItemName()
	{
		return this.name;
	}
	
	/**
	 * Retrieves the count of the inventory item
	 * @return The count of the inventory item
	 */
	public int getItemCount()
	{
		return this.count;
	}
	
	/**
	 * Retrieves the price of the inventory item
	 * @return The price of the inventory item
	 */
	public float getItemPrice()
	{
		return this.price;
	}
	
	/**
	 * Decreases the count of this item by one
	 */
	public void decreaseItemCount()
	{
		if(this.count > 0)
		{
			this.count -= 1;
		}
	}
	
	/**
	 * Builds and returns a formatted string of the item information
	 */
	@Override
	public String toString() 
	{
		return  "[" + getItemCount() + "] [" + getItemName() + "] [" + String.format("%.2f",getItemPrice()) + "]";
	}
}
