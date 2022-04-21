package response;

import enums.CliResponseType;

public class CliResponseGetItem extends CliResponse
{

	/** Item code to get */
	private String itemCode;
	
	/**
	 * Constructor defining a GET response type taking the item code to get
	 * 
	 * @param reqItemCode The item code to get
	 */
	public CliResponseGetItem(String reqItemCode) 
	{
		super(CliResponseType.GET);
		this.itemCode = reqItemCode;
	}
	
	/**
	 * Retrieves the item code
	 * @return The item code
	 */
	public String getItemCode()
	{
		return this.itemCode;
	}

}
