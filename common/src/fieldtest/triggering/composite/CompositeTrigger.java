package fieldtest.triggering.composite;

import java.util.ArrayList;
import java.util.List;

import fieldtest.triggering.FieldTestTrigger;
/**
 * A trigger implementing the disjunctive combination of multiple sub triggers.
 * That is, the method checktrigger() implements t1.checktrigger() || ...
 * tn.checktrigger() for trigger t1, ..., tn. 
 * 
 * It implements short-circuit evaluation which is relevant due to triggers 
 * having state.
 */
public class CompositeTrigger implements FieldTestTrigger{
	
	private List<FieldTestTrigger> internalTriggers = new ArrayList<>();
	private FieldTestTrigger lastActivatedTrigger = null;
	
	public void addTrigger(FieldTestTrigger trigger) {
		internalTriggers.add(trigger);
	}
	
	@Override
	public boolean checkTrigger() {
		for(FieldTestTrigger trigger : internalTriggers) {
			if(trigger.checkTrigger()) {
				lastActivatedTrigger = trigger;
				return true;
			}
		}
		return false;
	}

	@Override
	public List<String> additionalTriggerInformation() {
		List<String> additionalInfo = new ArrayList<String>();
		for(FieldTestTrigger trigger : internalTriggers) {
			if(trigger.additionalTriggerInformation() != null)
				additionalInfo.addAll(trigger.additionalTriggerInformation());
		}
		return additionalInfo;
	}

	@Override
	public String lastAdditionalTriggerInformation() {
		if(lastActivatedTrigger != null)
			return lastActivatedTrigger.lastAdditionalTriggerInformation();
		return null;
	}

}
