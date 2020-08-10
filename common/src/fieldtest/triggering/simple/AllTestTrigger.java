package fieldtest.triggering.simple;

import java.util.ArrayList;
import java.util.List;

import fieldtest.triggering.FieldTestTrigger;
/**
 * A field test trigger that triggers all possible field-test executions, i.e.,
 * the method <code>checkTrigger</code> returns always true.
 *
 */
public class AllTestTrigger implements FieldTestTrigger {

	@Override
	public boolean checkTrigger() {
		return true;
	}

	@Override
	public List<String> additionalTriggerInformation() {
		return new ArrayList<String>();
	}

	@Override
	public String lastAdditionalTriggerInformation() {
		return null;
	}

	
}
