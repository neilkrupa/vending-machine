package core;

import java.io.InputStreamReader;
import java.util.Scanner;

import enums.CliResponseType;
import enums.CoinType;
import interfaces.ICommandLineInterface;
import message.CliMessage;
import message.CliMessageDisplayDispenseChange;
import message.CliMessageDisplayDispenseItem;
import message.CliMessageDisplayInventoryItems;
import message.CliMessageRetrieveResponse;
import response.CliResponse;
import response.CliResponseAddFunds;
import response.CliResponseCoinUpdate;
import response.CliResponseGetItem;
import response.CliResponseInitChange;
import response.CliResponseRefund;
import message.CliMessageDisplayCustomerCredit;
import utils.CliPrintUtils;
import utils.CliUtils;

public class CommandLineInterface implements ICommandLineInterface {
	/** Scanner for user input */
	private Scanner scanner = new Scanner(new InputStreamReader(System.in));

	/**
	 * Constructor printing initialisation message
	 */
	public CommandLineInterface() {
		CliPrintUtils.printIntialsationMessage();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleCliMessage(CliMessage cliMessage) {
		switch (cliMessage.getMessageType()) {
		case DISPLAY_COMMAND_INFO:
			CliPrintUtils.printCommandInformation();
			break;
		case DISPLAY_CUSTOMER_CREDIT:
			CliPrintUtils.printCustomerCredit(((CliMessageDisplayCustomerCredit) cliMessage).toString());
			break;
		case DISPLAY_DISPENSE_CHANGE:
			CliMessageDisplayDispenseChange dispenseMessage = (CliMessageDisplayDispenseChange) cliMessage;
			CliPrintUtils.printDispenseChange(dispenseMessage.toString(), dispenseMessage.getSuccess());
			break;
		case DISPLAY_DISPENSE_ITEM:
			CliPrintUtils.printCliDispenseItem(((CliMessageDisplayDispenseItem) cliMessage).toString());
			break;
		case DISPLAY_INVENTORY:
			CliPrintUtils.printCliInventory(((CliMessageDisplayInventoryItems) cliMessage).toString());
			break;
		case RETRIEVE_RESPONSE:
		default:
			// Ignore these two cases as they should not display to the user.
			break;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CliResponse handleCliMessageForRequest(CliMessageRetrieveResponse cliMessage) {
		switch (cliMessage.getInputType()) {
		case CHANGE_CHECK:
			return checkChangeValues(cliMessage.toString());
		case COMMAND:
			return requestUserCommand();
		case INITIAL_CHANGE:
			return requestInitialChangeValue();
		default:
			return new CliResponse();
		}
	}

	/**
	 * Requests the initial change value, validates the input and returns the value
	 * as a CLIResponse
	 * 
	 * @return The initial change value as a CLIResponse
	 */
	public CliResponse requestInitialChangeValue() {

		CliPrintUtils.printInitialChange();
		float changeVal = -1;
		while (changeVal == -1) {
			changeVal = CliUtils.validateRequestedFloatInput(scanner.nextLine());
			if (changeVal == -1) {
				CliPrintUtils.printInvalidInitialChange();
			}
		}
		return new CliResponseInitChange(changeVal);
	}

	/**
	 * Requests the user command input and returns the command response as a
	 * CliResponse
	 * 
	 * @return The validated user input command
	 */
	public CliResponse requestUserCommand() {
		CliPrintUtils.printCliExpectInput();
		boolean validInput = false;
		String input = "";
		while (!validInput) {
			input = scanner.nextLine();
			validInput = CliUtils.validateCommandInput(input);
		}
		return generateRequestFromUserInput(CliUtils.getResponseTypeFromInput(input),
				CliUtils.getRequestValueFromInput(input));
	}

	/**
	 * Generates the request from the user input
	 * 
	 * @param type  Type of response
	 * @param value Value for the response object
	 * @return Response to the vending machine instance
	 */
	private CliResponse generateRequestFromUserInput(CliResponseType type, Object value) {
		switch (type) {
		case ADD:
			if (value instanceof CoinType) {
				return new CliResponseAddFunds((CoinType) value);
			}
		case GET:
			if (value instanceof String) {
				return new CliResponseGetItem((String) value);
			}
		case INIT_CHANGE:
			if (value instanceof String) {
				return new CliResponseGetItem((String) value);
			}
		case INIT_COIN:
			if (value instanceof String) {
				return new CliResponseGetItem((String) value);
			}
		case REFUND:
			return new CliResponseRefund();
		default:
			return null;
		}
	}

	/**
	 * Asks the user to check the calculated values for the change and whether they
	 * would like to update the coin count for each coin
	 * 
	 * @param message Message containing current change values
	 * @return Response containing a change request or an empty request depending on
	 *         user input
	 */
	public CliResponse checkChangeValues(String message) {
		CliPrintUtils.printChangeCheck(message);
		boolean setValues = CliUtils
				.askForYesNoInput("Would you like to manually set the count for either all coins or a specific coin type?", scanner);
		if (setValues) {
			CliPrintUtils.printSetCoinCountInfo();

			boolean checking = true;
			while (checking) {
				String value = scanner.nextLine();
				int validReturn = CliUtils.ValidateCheckChangeInput(value);
				if (validReturn == 0) {
					checking = false;
				} else if (validReturn == 1) {
					String[] valueSplit = value.split("=");
					int count = Integer.parseInt(valueSplit[1]);
					return new CliResponseCoinUpdate(count);
				} else if (validReturn == 2) {
					String[] valueSplit = value.split("=");
					CoinType coin = CoinType.valueOf(valueSplit[0].trim().toUpperCase());
					int count = Integer.parseInt(valueSplit[1]);
					return new CliResponseCoinUpdate(coin, count);
				}
			}
		}
		return new CliResponse();
	}
}
