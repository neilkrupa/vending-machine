package message;

import enums.CliMessageType;

public class CliMessageDisplayInventoryItems extends CliMessage {
	/** Comma separated items in inventory message */
	private String inventory;

	/**
	 * Constructor defining a DISPLAY_INVENTORY message type taking in comma
	 * separated items in inventory message
	 * 
	 * @param invMessage Comma separated items in inventory message
	 */
	public CliMessageDisplayInventoryItems(String invMessage) {
		super(CliMessageType.DISPLAY_INVENTORY);
		this.inventory = invMessage;
	}

	/**
	 * Returns the comma separated items in inventory message
	 */
	@Override
	public String toString() {
		return this.inventory;
	}
}
