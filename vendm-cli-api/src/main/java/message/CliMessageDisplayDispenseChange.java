package message;

import enums.CliMessageType;

public class CliMessageDisplayDispenseChange extends CliMessage
{
	/** Message defining change */
	private String message;
	
	/** Dispense change success or failure */
	private boolean success;
	
	/**
	 * Constructor defining a DISPLAY_DISPENSE_CHANGE message type taking in message
	 * defining change and success of dispense
	 * 
	 * @param chngMessage Message defining change
	 * @param success     Dispense change success or failure
	 */
	public CliMessageDisplayDispenseChange(String chngMessage, boolean success)
	{
		super(CliMessageType.DISPLAY_DISPENSE_CHANGE);
		this.message = chngMessage;
		this.success = success;
	}
	
	/**
	 * Retrieves whether the dispense of change was successful or failed
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
