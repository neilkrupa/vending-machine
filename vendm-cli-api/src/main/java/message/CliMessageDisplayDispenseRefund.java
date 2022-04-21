package message;

import enums.CliMessageType;

public class CliMessageDisplayDispenseRefund extends CliMessage
{
	/** Message defining refund */
	private String message;
	
	/**
	 * Constructor defining a DISPLAY_DISPENSE_REFUND message type taking in message
	 * defining refund
	 * 
	 * @param itemMessage Message defining refund
	 */
	public CliMessageDisplayDispenseRefund(String rfndMessage)
	{
		super(CliMessageType.DISPLAY_DISPENSE_REFUND);
		this.message = rfndMessage;
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
