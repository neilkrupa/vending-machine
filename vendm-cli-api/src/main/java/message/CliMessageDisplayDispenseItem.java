package message;

import enums.CliMessageType;

public class CliMessageDisplayDispenseItem extends CliMessage
{
	/** Message defining item */
	private String message;
	
	/** Dispense item success or failure */
	private boolean success;
	
	/**
	 * Constructor defining a DISPLAY_DISPENSE_ITEM message type taking in message
	 * defining item and success of dispense
	 * 
	 * @param itemMessage Message defining item
	 * @param success     Dispense item success or failure
	 */
	public CliMessageDisplayDispenseItem(String itemMessage, boolean success)
	{
		super(CliMessageType.DISPLAY_DISPENSE_ITEM);
		this.message = itemMessage;
		this.success = success;
	}
	
	/**
	 * Retrieves whether the dispense of item was successful or failed
	 * @return True on success, false on failure
	 */
	public boolean getSuccess()
	{
		return this.success;
	}
	
	/**
	 * Returns the message
	 */
	@Override
	public String toString() 
	{
		return this.message;
	}
}
