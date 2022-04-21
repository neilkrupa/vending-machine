package exceptions;

/**
 * This exception is to be used when a coin cannot be broken down into smaller
 * change
 * 
 * @author neilk
 *
 */
public class ChangeCalculationException extends Exception {

	/** Generated SVUID */
	private static final long serialVersionUID = 3171424521475672476L;

	/**
	 * Constructor
	 * 
	 * @param message Exception message
	 */
	public ChangeCalculationException(String message) {
		super(message);
	}
}
