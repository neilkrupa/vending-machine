package message;

import enums.CliMessageType;

public class CliMessage 
{
	/** Type of cli message */
	private CliMessageType msgType;
	
	/**
	 * Constructor 
	 * @param cliMsgType Type of cli message
	 */
	public CliMessage(CliMessageType cliMsgType)
	{
		this.msgType = cliMsgType;
	}
	
	/**
	 * Retrieves the cli message type
	 * @return The cli message type
	 */
	public CliMessageType getMessageType()
	{
		return this.msgType;
	}
}
