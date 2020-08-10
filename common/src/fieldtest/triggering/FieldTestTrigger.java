package fieldtest.triggering;

import java.util.List;

/**
 * The interface to triggers (also referred to as triggering mechanisms). 
 * 
 * It allows to check a field test-case should be triggered and to retrieve 
 * additional information about the triggering history. 
 *
 */
public interface FieldTestTrigger {
	/**
	 * Method for checking whether to trigger a field test-case.
	 * 
	 * @return true if field test-case should be triggered and false otherwisey
	 */
	boolean checkTrigger();

	/**
	 * Additional on all previous instances where the triggering mechanism was
	 * accessed.
	 * 
	 * @return a list of strings with triggering information with no sprecific
	 * structure
	 */
	List<String> additionalTriggerInformation();
	// return null if there is no information
	/**
	 * Additional information on the last time a field test-case was triggered.
	 * 
	 * @return string with triggering information or null if there is no 
	 * relevant information (e.g., if last field test-case has not been 
	 * triggered)
	 */
	String lastAdditionalTriggerInformation();
}
