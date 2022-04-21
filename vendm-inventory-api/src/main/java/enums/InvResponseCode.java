package enums;

/**
 * This enumeration defines the response code for the item in stock check for
 * the inventory processing
 * 
 * @author neilk
 *
 */
public enum InvResponseCode {
	OK, 
	ITEM_DOESNT_EXIST, 
	ITEM_SOLD_OUT;
}
