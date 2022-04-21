package utils;

import java.util.Scanner;

import enums.CliResponseType;
import enums.CoinType;

/**
 * This class provides utilities for the CLI
 * @author neilk
 *
 */
public class CliUtils {
	
	/*********************/
	/***** Validation ****/
	/*********************/
	
	public static float validateRequestedFloatInput(String initChangeStrVal) {
		try {
			// Ensure the value can be converted to a float
			float initialChangeValue = Float.parseFloat(initChangeStrVal);
			// Ensure the value has only 2 decimal places and is greater than zero
			String[] splitChangeStr = initChangeStrVal.split("\\.");
			if (initialChangeValue < 0 || splitChangeStr.length > 2 || splitChangeStr[1].length() > 2) {
				throw new Exception();
			}
			return initialChangeValue;
		} catch (Exception e) {
			return -1;
		}
	}
	
	public static boolean validateCommandInput(String input) {
		String[] inputSplit = input.split(" ");
		if (inputSplit.length >= 3 || !inputSplit[0].trim().toUpperCase().equals(CliResponseType.ADD.name())
				&& !inputSplit[0].trim().toUpperCase().equals(CliResponseType.GET.name())
				&& !inputSplit[0].trim().toUpperCase().equals(CliResponseType.REFUND.name()))
		{
			CliPrintUtils.printCliLineBreak();
			CliPrintUtils.printCliFormattedMessageForInput("Invalid command. Please try again.");
			return false;
		}

		String errorMessage = "";
		if (inputSplit[0].trim().toUpperCase().equals(CliResponseType.ADD.name())) {
			errorMessage = "Malformed 'add' command: ";
			errorMessage += "Unable to validate coin type. ";
			for (CoinType coin : CoinType.values()) {
				if (inputSplit[1].trim().toUpperCase().equals(coin.name())) {
					return true;
				}
				if (CliUtils.validateRequestedFloatInput(inputSplit[1].trim()) == coin.getValue()) {
					return true;
				}
			}
			errorMessage += "Unable to validate float value";
		} else if (inputSplit[0].trim().toUpperCase().equals(CliResponseType.GET.name())) {
			// Validation for this will depend on the items in the inventory and will be
			// done in the instance
			return true;
		}
		else if (inputSplit[0].trim().toUpperCase().equals(CliResponseType.REFUND.name())) {
			return true;
		}
		CliPrintUtils.printCliLineBreak();
		CliPrintUtils.printCliFormattedMessageForInput("Command error: " + errorMessage);
		return false;
	}
	
	/*********************/
	/******* Output ******/
	/*********************/
	
	public static boolean askForYesNoInput(String message, Scanner scanner) {
		CliPrintUtils.printCliFormattedMessageForInput(message + " (y/n)");
		String correct = "";
		while (!correct.equals("y") && !correct.equals("n")) {
			correct = scanner.nextLine();
			if (correct.equals("y")) {
				return true;
			} else if (correct.equals("n")) {
				return false;
			} else {
				CliPrintUtils.printCliLineBreak();
				CliPrintUtils.printCliFormattedMessageForInput("Unknown entry, please enter one of the following inputs: (y/n)");
			}
		}
		return false;
	}

	/*********************/
	/***** Extraction ****/
	/*********************/
	
	public static CliResponseType getResponseTypeFromInput(String input) 
	{
		String[] inputSplit = input.split(" ");
		String command = inputSplit[0].trim().toUpperCase();
		if(command.equals(CliResponseType.ADD.name()))
		{
			return CliResponseType.ADD;
		}
		else if(command.equals(CliResponseType.GET.name()))
		{
			return CliResponseType.GET;
		}
		else if(command.equals(CliResponseType.REFUND.name()))
		{
			return CliResponseType.REFUND;
		}
		return null;
	}

	public static Object getRequestValueFromInput(String input) 
	{
		String[] inputSplit = input.split(" ");
		CliResponseType type  = getResponseTypeFromInput(input);
		String value = null;
		if(inputSplit.length >= 2)
		{
			value = inputSplit[1].trim();
		}
		
		switch (type) 
		{
			case ADD:
				for (CoinType coin : CoinType.values()) {
					if (value.toUpperCase().equals(coin.name())) {
						
						return coin;
					}
					if (CliUtils.validateRequestedFloatInput(value) == coin.getValue()) {
						
						return coin;
					}
				}
				return null;
			case GET:
				return value;
			case REFUND:
				return null;
			default:
				return null;
		}
	}

	public static int ValidateCheckChangeInput(String value) {
		try
		{
			String[] valueSplit = value.split("=");
			if(valueSplit[0].equals("continue"))
			{
				return 0;
			}
			else if(valueSplit[0].equals("ALL"))
			{
				Integer.parseInt(valueSplit[1]);
				return 1;
			}
			else 
			{
				boolean validCoinType = false;
				for (CoinType coin : CoinType.values()) {
					if (valueSplit[0].trim().toUpperCase().equals(coin.name())) {
						validCoinType =  true;
					}
					if(validCoinType)
					{
						Integer.parseInt(valueSplit[1]);
						return 2;
					}
				}
			}
		}
		catch(Exception e)
		{
			// Do nothing
		}
		CliPrintUtils.printInvalidChangeCheck();
		return -1;
	}
}
