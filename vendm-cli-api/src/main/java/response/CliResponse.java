package response;

import enums.CliResponseType;

public class CliResponse {
	/** Type of response enum */
	private CliResponseType type;

	/**
	 * Constructor taking in response type
	 * 
	 * @param responseType Type of response for this message
	 */
	public CliResponse(CliResponseType responseType) {
		this.type = responseType;
	}

	/**
	 * Constructor defining an EMPTY response type
	 */
	public CliResponse() {
		this.type = CliResponseType.EMPTY;
	}

	/**
	 * Retrieves the response type enum
	 * 
	 * @return The response type enum
	 */
	public CliResponseType getResponseType() {
		return type;
	}
}
