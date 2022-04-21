/**
 * The core package contains all of the vending machine core classes
 */
package core;

import java.util.logging.Logger;

import enums.InvResponseCode;
import enums.ChanageResponseCode;
import enums.CliMessageType;
import enums.VendmState;
import interfaces.IChangeManager;
import interfaces.ICommandLineInterface;
import interfaces.IInventoryManager;
import message.CliMessage;
import message.CliMessageDisplayDispenseChange;
import message.CliMessageDisplayDispenseItem;
import message.CliMessageDisplayInventoryItems;
import message.CliMessageRetrieveResponse;
import message.CliMessageRetrieveResponse.InputType;
import response.CliResponse;
import response.CliResponseAddFunds;
import response.CliResponseCoinUpdate;
import response.CliResponseGetItem;
import response.CliResponseInitChange;
import response.CliResponseRefund;
import utils.ChangeUtils;
import message.CliMessageDisplayCustomerCredit;

public class VendmInstance {

	/** Logger for this class */
	private static final Logger LOG = Logger.getLogger(VendmInstance.class.getName());

	/** Logging tag for this class */
	private static final String LOG_TAG = "[" + VendmInstance.class.getName() + "]: ";

	/** Vending machine state */
	private static VendmState state = VendmState.INITIALISE;

	/** Vending machine instance */
	private static VendmInstance instance = null;

	/** Vending machine core interfaces */
	private IChangeManager chManager;
	private IInventoryManager invManager;
	private ICommandLineInterface cli;

	/**
	 * This private constructor is used to create a singleton instance of this
	 * class. After initialisation this instance will wait for user input until its
	 * state changes
	 */
	private VendmInstance() {
		initialise();
		while (state == VendmState.RUNNING) {
			handleUserInput();
		}
	}

	/**
	 * This method is used to create and return a singleton instance of this class.
	 * 
	 * @return Singleton instance of this class
	 */
	public static VendmInstance getInstance() {
		if (instance == null) {
			instance = new VendmInstance();
		}
		return instance;
	}

	/**
	 * This function will initialise the inventory manager and change manager while
	 * registering its self as a cli communicator to each. It will request user
	 * input for the initialisation of the change manager to retrieve the initial
	 * value for the change stored in this machine. If an exception is thrown during
	 * initialisation, an error will be logged and the system will exit.
	 */
	private void initialise() {
		try {
			LOG.finest(LOG_TAG + "Initialising inventory manager");
			invManager = new InventoryManager();

			LOG.finest(LOG_TAG + "Initialising command line interface");
			cli = new CommandLineInterface();

			LOG.finest(LOG_TAG + "Retrieving value from user");
			CliResponse initialChangeReqRes = sendCliMessageForResponse(
					new CliMessageRetrieveResponse(InputType.INITIAL_CHANGE));
			float initialChange = ((CliResponseInitChange) initialChangeReqRes).getChangeVal();

			LOG.finest(LOG_TAG + "Initialising change manager");
			chManager = new ChangeManager(initialChange);

			LOG.finest(LOG_TAG + "Check change values");
			CliResponse checkChangeReqRes = sendCliMessageForResponse(
					new CliMessageRetrieveResponse(chManager.getChangeInMachineAsString()));

			boolean changeValuesAccepted = false;
			while (!changeValuesAccepted) {
				if (checkChangeReqRes instanceof CliResponseCoinUpdate) {
					CliResponseCoinUpdate response = ((CliResponseCoinUpdate) checkChangeReqRes);
					chManager.updateCoinCount(response.getCoinType(), response.getCoinCount());
					checkChangeReqRes = sendCliMessageForResponse(
							new CliMessageRetrieveResponse(chManager.getChangeInMachineAsString()));
					continue;
				}
				changeValuesAccepted = true;
			}

			LOG.finest(LOG_TAG + "Initialisation complete");
			state = VendmState.RUNNING;

			LOG.finest(LOG_TAG + "Displaying command infomation to user");
			sendCliMessage(new CliMessage(CliMessageType.DISPLAY_COMMAND_INFO));

			LOG.finest(LOG_TAG + "Display inventory and await user input");
			sendCliMessage(new CliMessageDisplayInventoryItems(invManager.getDisplayDataInventoryItems()));
			
			LOG.finest(LOG_TAG + "Display customer credit");
			sendCliMessage(new CliMessageDisplayCustomerCredit(chManager.getCustomerCredit()));

		} catch (Exception e) {
			LOG.severe(LOG_TAG + "Unable to initialise the vending machine due " + "to the following errors: "
					+ e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * This blocking method communicates with the CLI requesting user input and
	 * handles the response. The response is will be a type defined in the
	 * CliRequestType enumeration.
	 */
	private void handleUserInput() {
		CliResponse message = sendCliMessageForResponse(new CliMessageRetrieveResponse(InputType.COMMAND));
		switch (message.getResponseType()) {
		case ADD:
			handleRequestAdd((CliResponseAddFunds) message);
			break;
		case GET:
			handleRequestGet((CliResponseGetItem) message);
			break;
		case REFUND:
			handleRefund((CliResponseRefund) message);
			break;
		default:
			break;
		}
	}

	/**
	 * This method handles the Cli request to insert a specific coin into the
	 * vending machine. The coin type is extracted from the request and forwarded to
	 * the change manager where it is processed.
	 * 
	 * @param insertRequest Insert request from the CLI
	 */
	private void handleRequestAdd(CliResponseAddFunds insertRequest) {
		float customerCredit = chManager.depositCoin(insertRequest.getCoinType());
		CliMessageDisplayCustomerCredit creditMessage = new CliMessageDisplayCustomerCredit(customerCredit);
		sendCliMessage(creditMessage);
	}

	/**
	 * This method handles the cli get request checking whether the item processing
	 * and change processing return success before dispensing the item
	 * 
	 * @param buyRequest Buy request from the CLI
	 */
	private void handleRequestGet(CliResponseGetItem buyRequest) {
		String itemCode = buyRequest.getItemCode();
		if (processInventory(itemCode) && processChange(itemCode)) {
			dispense(itemCode);
		}
	}

	/**
	 * This method handles the refunding of all user entered change and sends a
	 * display message to the cli
	 * 
	 * @param message Refund message from the CLI
	 */
	private void handleRefund(CliResponseRefund message) {
		sendCliMessage(
				new CliMessageDisplayDispenseChange(ChangeUtils.getCoinMapAsString(chManager.dispenseRefund()), true));
	}

	/**
	 * This method checks whether the item exists in the inventory, has sold out, or
	 * is ok to dispense. It will send a display message to the cli on failure.
	 * 
	 * @param itemCode Code of the item to check
	 * @return True if okay to dispense, false otherwise
	 */
	private boolean processInventory(String itemCode) {
		InvResponseCode inventoryResponse = invManager.itemInStock(itemCode);

		String message = "";
		switch (inventoryResponse) {
		case OK:
			return true;
		case ITEM_SOLD_OUT:
			message = "Item for the code [" + itemCode + "] is not in stock";
			break;
		case ITEM_DOESNT_EXIST:
			message = "Item for the code [" + itemCode + "] does not exist";
			break;
		default:
			message = "Item for the code [" + itemCode + "] is currently unavailable";
			break;
		}
		sendCliMessage(new CliMessageDisplayDispenseItem(message, false));
		return false;
	}

	/**
	 * This method checks whether the customer has deposited enough change into the
	 * vending machine for the requested item, whether the machine has enough change
	 * if it is required or whether the funds are fine. It will send a display
	 * message to the cli on failure.
	 * 
	 * @param itemCode Item code to check the price of
	 * @return True if funds are fine, false otherwise
	 */
	private boolean processChange(String itemCode) {
		float itemPrice = invManager.getItemPrice(itemCode);
		ChanageResponseCode responseCode = chManager.checkAvailableFunds(itemPrice);

		String message = "";
		switch (responseCode) {
		case OK:
			return true;
		case MORE_FUNDS_REQUIRED:
			message = "Customer needs to insert more funds - Credit is: " + chManager.getCustomerCredit();
			break;
		case CHANGE_UNAVAILABLE:
			message = "Change currently unavailable for this transaction. Please add smaller change or refund the item.";
			message += "-Change required is " + (chManager.getCustomerCredit() - itemPrice);
			message += "-Change available is:-" + chManager.getChangeInMachineAsString();
			break;
		default:
			message = "Item for the code [" + itemCode + "] is currently unavailable";
			break;
		}
		sendCliMessage(new CliMessageDisplayDispenseChange(message, false));
		return false;
	}

	/**
	 * This method decreases the number of the item in the inventory and sends a cli
	 * message, processes the change and sends a dispense message to the cli
	 * 
	 * @param itemCode Item code to process
	 */
	private void dispense(String itemCode) {
		float itemPrice = invManager.getItemPrice(itemCode);

		sendCliMessage(new CliMessage(CliMessageType.DISPLAY_COMMAND_INFO));
		
		
		
		// Handle inventory dispensing and message sending
		String dispenseMessage = invManager.dispenseItem(itemCode);
		sendCliMessage(new CliMessageDisplayInventoryItems(invManager.getDisplayDataInventoryItems()));
		sendCliMessage(new CliMessageDisplayDispenseItem(dispenseMessage, true));

		// Handle change dispensing and message sending
		sendCliMessage(new CliMessageDisplayDispenseChange(
				ChangeUtils.getCoinMapAsString(chManager.dispenseChange(itemPrice)), true));
		
		sendCliMessage(new CliMessageDisplayCustomerCredit(chManager.getCustomerCredit()));
		
		
	}

	public void sendCliMessage(CliMessage message) {
		cli.handleCliMessage(message);
	}

	public CliResponse sendCliMessageForResponse(CliMessageRetrieveResponse message) {
		return cli.handleCliMessageForRequest(message);
	}
}
