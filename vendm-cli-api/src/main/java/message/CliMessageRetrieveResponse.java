package message;

import enums.CliMessageType;

public class CliMessageRetrieveResponse extends CliMessage {
	/** Enumeration defining the input type */
	public static enum InputType {
		INITIAL_CHANGE, CHANGE_CHECK, COMMAND
	}

	/** Message input type */
	private InputType type;

	/** Message for CHANGE_CHECK */
	private String message = "";

	/**
	 * Constructor defining a RETRIEVE_RESPONSE message type taking input type
	 * 
	 * @param inputType Input type enum
	 */
	public CliMessageRetrieveResponse(InputType inputType) {
		super(CliMessageType.RETRIEVE_RESPONSE);
		this.type = inputType;
	}

	/**
	 * Constructor for CHANGE_CHECK taking in formatted change message for display
	 * chaining constructor
	 * 
	 * @param message Change in machine formatted message for display
	 */
	public CliMessageRetrieveResponse(String message) {
		this(InputType.CHANGE_CHECK);
		this.message = message;
	}

	/**
	 * Retrieves the input type
	 * 
	 * @return The input type of the message
	 */
	public InputType getInputType() {
		return this.type;
	}

	/**
	 * Retrieves the message for a CHANGE_CHECK otherwise returns {@inheritDoc}
	 */
	@Override
	public String toString() {
		if (this.type == InputType.CHANGE_CHECK) {
			return message;
		} else {
			return super.toString();
		}
	}
}
