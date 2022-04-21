package response;

import enums.CliResponseType;

public class CliResponseInitChange extends CliResponse {
	/** Initial change value */
	private float changeVal;

	/**
	 * Constructor defining a INIT_CHANGE response type which takes in the initial
	 * change value
	 * 
	 * @param changeVal Initial change value to set
	 */
	public CliResponseInitChange(float changeVal) {
		super(CliResponseType.INIT_CHANGE);
		this.changeVal = changeVal;
	}

	/**
	 * Retrieves the initial change value
	 * 
	 * @return The initial change value
	 */
	public float getChangeVal() {
		return this.changeVal;
	}
}
