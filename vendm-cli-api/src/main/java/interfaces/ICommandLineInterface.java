package interfaces;

import message.CliMessage;
import message.CliMessageRetrieveResponse;
import response.CliResponse;

public interface ICommandLineInterface 
{
	/**
	 * This method processes an incoming CLI message that required no response
	 * @param message Message to process
	 */
	public void handleCliMessage(CliMessage message);
	
	/**
	 * This method processes an incoming CLI message that requires a response
	 * @param cliMessage Message to process for a response
	 * @return Response message
	 */
	public CliResponse handleCliMessageForRequest(CliMessageRetrieveResponse cliMessage);
}
