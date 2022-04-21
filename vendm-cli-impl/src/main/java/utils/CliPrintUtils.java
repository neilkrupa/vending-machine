package utils;

import java.util.ArrayList;
import java.util.List;

import enums.CoinType;

/**
 * This class defines CLI output messages
 * @author neilk
 *
 */
public class CliPrintUtils {

	public static void printIntialsationMessage() {
		printCliLineBreak();
		printCliFormattedMessage("Initialising vending machine - Input is required to complete setup...");
		printCliLineBreak();
	}

	public static void printInitialChange() {
		CliPrintUtils.printCliFormattedMessage("Please enter an initial change value for the vending machine: ");
		CliPrintUtils.printCliFormattedMessage(" ");
		CliPrintUtils.printCliFormattedMessageForInput(
				"Initial change value must be a float with a value of zero or greater "
						+ "containing two decimal places (i.e. \"11.76\")");
	}

	public static void printInvalidInitialChange() {
		CliPrintUtils.printCliLineBreak();
		CliPrintUtils.printCliFormattedMessageForInput("Initial change value must be a float with a value of "
				+ "zero or greater containing two decimal places (i.e. \"11.76\").");
	}

	public static void printChangeCheck(String changeCheckStr) {
		CliPrintUtils.printCliLineBreak();
		String[] values = changeCheckStr.split(",");
		for (int i = 0; i <= values.length; i = i + 2) {
			if (i < values.length - 2) {
				printCliFormattedMessageNoEndMarker(values[i] + "\t" + values[i + 1]);
			} else {
				CliPrintUtils.printCliFormattedMessageNoEndMarker(" ");
				printCliFormattedMessageNoEndMarker(values[i]);
			}
		}
		CliPrintUtils.printCliFormattedMessageNoEndMarker(" ");
		CliPrintUtils.printCliFormattedMessageNoEndMarker("Please check the above change values");
		CliPrintUtils.printCliLineBreak();
	}

	public static void printInvalidChangeCheck() {
		CliPrintUtils.printCliLineBreak();
		CliPrintUtils.printCliFormattedMessage("Invalid command - Please try again.");
		CliPrintUtils.printCliFormattedMessage("[EXAMPLE]: ONE_POUND=10");
		CliPrintUtils.printCliLineBreak();
		CliPrintUtils.printCliExpectInput();
	}

	public static void printSetCoinCountInfo() {
		CliPrintUtils.printCliLineBreak();
		CliPrintUtils.printCliFormattedMessage("To skip this setup type 'continue'");
		CliPrintUtils.printCliFormattedMessage("To set a coin count please enter a command in the following format: ");
		CliPrintUtils.printCliFormattedMessage(" ");
		CliPrintUtils.printCliFormattedMessage("  <coin-type>=<coin-count> ");
		CliPrintUtils.printCliFormattedMessage(" ");
		CliPrintUtils.printCliFormattedMessage("Available coin types are: ");
		for (CoinType coin : CoinType.values()) {
			CliPrintUtils.printCliFormattedMessage("  " + coin.name());
		}
		CliPrintUtils.printCliFormattedMessage("  ALL");
		CliPrintUtils.printCliLineBreak();
		CliPrintUtils.printCliExpectInput();
	}

	public static void printCommandInformation() {
		CliPrintUtils.printCliLineBreak();
		CliPrintUtils.printCliFormattedMessage("Available commands are as follows:");
		CliPrintUtils.printCliFormattedMessage(" ");
		CliPrintUtils.printCliFormattedMessage("add <coin-type>      Add a coin to the vending machine");
		CliPrintUtils.printCliFormattedMessage("add <float-value>    Add a coin value to the vending machine");
		CliPrintUtils.printCliFormattedMessage(" ");
		for (CoinType coin : CoinType.values()) {
			CliPrintUtils
					.printCliFormattedMessage("  " + coin.name() + " [" + String.format("%.2f", coin.getValue()) + "]");
		}
		CliPrintUtils.printCliFormattedMessage(" ");
		CliPrintUtils.printCliFormattedMessage(
				"get <item-code>      Retrieve an item and any change using item code from the list above");
		CliPrintUtils.printCliFormattedMessage("refund               Refund credit stored in the machine");
		CliPrintUtils.printCliLineBreak();
	}

	public static void printCustomerCredit(String credit) {
		CliPrintUtils.printCliLineBreak();
		CliPrintUtils.printCliFormattedMessage(credit);
		CliPrintUtils.printCliLineBreak();
	}

	public static void printCliInventory(String inventoryConcat) {
		CliPrintUtils.printCliLineBreak();
		CliPrintUtils.printCliFormattedMessageNoEndMarker("Code \t No \t Name \t\t\t Price");
		String[] inventory = inventoryConcat.split(",");
		for(String item : inventory)
		{
			String[] inventoryItem = item.split(" ");
			if(inventoryItem[2].length() < 16)
			{
				CliPrintUtils.printCliFormattedMessageNoEndMarker(inventoryItem[0] + "\t" + inventoryItem[1] + "\t" + inventoryItem[2] + "\t\t" + inventoryItem[3]);
			}
			else
			{
				CliPrintUtils.printCliFormattedMessageNoEndMarker(inventoryItem[0] + "\t" + inventoryItem[1] + "\t" + inventoryItem[2] + "\t" + inventoryItem[3]);
			}
			
		}
		CliPrintUtils.printCliLineBreak();
	}

	public static void printDispenseChange(String change, boolean success) {
		CliPrintUtils.printCliLineBreak();
		if(success)
		{
			String[] values = change.split(",");
			for (int i = 0; i <= values.length; i = i + 2) {
				if (i < values.length - 2) {
					printCliFormattedMessageNoEndMarker(values[i] + "\t" + values[i + 1]);
				} else {
					CliPrintUtils.printCliFormattedMessageNoEndMarker(" ");
					printCliFormattedMessageNoEndMarker(values[i]);
				}
			}
		}
		else
		{
			
			String[] values = change.split("-");
			printCliFormattedMessageNoEndMarker(values[0]);
			printCliFormattedMessageNoEndMarker(values[1]);
			printCliFormattedMessageNoEndMarker(values[2]);
			String[] availChange = values[3].split(",");
			for (int i = 0; i <= availChange.length; i = i + 2) {
				if (i < availChange.length - 2) {
					printCliFormattedMessageNoEndMarker(availChange[i] + "\t" + availChange[i + 1]);
				} else {
					break;
				}
			}
		}
		CliPrintUtils.printCliLineBreak();
	}

	public static void printCliDispenseItem(String item) {
		CliPrintUtils.printCliLineBreak();
		CliPrintUtils.printCliFormattedMessageNoEndMarker(item);
		CliPrintUtils.printCliLineBreak();
	}

	public static void printCliLineBreak() {
		System.out.printf("| %-100.100s |%n", "--------------------------------------------------------"
				+ "--------------------------------------------");
	}

	public static void printCliFormattedMessageForInput(String input) {
		printCliFormattedMessage(input);
		printCliLineBreak();
		printCliExpectInput();
	}

	public static void printCliExpectInput() {
		System.out.print(">:");
	}

	public static void printCliFormattedMessage(String input) {
		int partitionSize = 100;
		List<String> parts = new ArrayList<String>();
		int len = input.length();
		for (int i = 0; i < len; i += partitionSize) {
			parts.add(input.substring(i, Math.min(len, i + partitionSize)));
		}

		for (String output : parts) {
			System.out.printf("| %-100.100s | %n", output);
		}
	}

	public static void printCliFormattedMessageNoEndMarker(String input) {
		int partitionSize = 100;
		List<String> parts = new ArrayList<String>();
		int len = input.length();
		for (int i = 0; i < len; i += partitionSize) {
			parts.add(input.substring(i, Math.min(len, i + partitionSize)));
		}

		for (String output : parts) {
			System.out.printf("| %-100.100s %n", output);
		}
	}

}
