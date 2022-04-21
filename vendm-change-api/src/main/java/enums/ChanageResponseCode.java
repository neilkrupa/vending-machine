package enums;

/**
 * This enumeration defines the response codes when checking available funds. It
 * defines whether the customer has not deposited enough funds, there is not
 * enough change in the machine or everything is ok.
 * 
 * @author neilk
 *
 */
public enum ChanageResponseCode {
	OK, 
	MORE_FUNDS_REQUIRED, 
	CHANGE_UNAVAILABLE;
}
