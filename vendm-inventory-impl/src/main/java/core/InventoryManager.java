package core;

import java.util.LinkedHashMap;

import enums.InvItemsEnum;
import enums.InvResponseCode;
import interfaces.IInventoryManager;
import types.InventoryItem;

public class InventoryManager implements IInventoryManager {

	/** Maps an item code to an item in the inventory maintaining insertion order */
	private LinkedHashMap<String, InventoryItem> inventory = new LinkedHashMap<String, InventoryItem>();

	/**
	 * Constructor adds all of the items defined in the InvItemsEnum to the
	 * inventory
	 */
	public InventoryManager() {
		String row = "A";
		int column = 1;
		int charValue = row.charAt(0);

		for (InvItemsEnum item : InvItemsEnum.values()) {
			String itemCode = row + column;
			inventory.put(itemCode, new InventoryItem(item.name(), item.getCount(), item.getPrice()));
			column++;

			if (column == 5) {
				row = String.valueOf((char) (charValue + 1));
				column = 1;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InvResponseCode itemInStock(String itemCode) {
		if (!inventory.containsKey(itemCode)) {
			return InvResponseCode.ITEM_DOESNT_EXIST;
		} else if (inventory.get(itemCode).getItemCount() <= 0) {
			return InvResponseCode.ITEM_SOLD_OUT;
		} else {
			return InvResponseCode.OK;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getItemPrice(String itemCode) {
		return inventory.get(itemCode).getItemPrice();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String dispenseItem(String itemCode) {
		inventory.get(itemCode).decreaseItemCount();
		return "Despensing item " + inventory.get(itemCode).getItemName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDisplayDataInventoryItems() {
		// Building inventory message
		String inventoryStr = "";
		if (inventory.keySet().isEmpty()) {
			inventoryStr += "Vending machine is empty";
		} else {
			for (String itemCode : inventory.keySet()) {
				inventoryStr += "[" + itemCode + "] " + inventory.get(itemCode).toString() + ",";
			}
		}
		return inventoryStr;
	}
}
