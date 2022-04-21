package message;

import enums.CliMessageType;

public class CliMessageDisplayCustomerCredit extends CliMessage {
	/** User credit available */
	private float credit;

	/**
	 * Constructor with message type DISPLAY_CUSTOMER_CREDIT taking customer credit
	 * as input
	 * 
	 * @param custCredit Customer credit
	 */
	public CliMessageDisplayCustomerCredit(float custCredit) {
		super(CliMessageType.DISPLAY_CUSTOMER_CREDIT);
		this.credit = custCredit;
	}

	/**
	 * Retrieve the customer credit
	 * 
	 * @return
	 */
	public float getCustomerCredit() {
		return this.credit;
	}

	/**
	 * Formatted message for display
	 */
	@Override
	public String toString() {
		return "Customer credit: " + String.format("%.2f", this.credit);
	}
}
