package fieldtest.triggering;

import java.util.List;

/**
 * A singleton holding an object implementing a triggering mechanism. 
 * 
 * It also computes the time required by each check of a trigger. * 
 *
 */
public class TriggerSingleton {

	private static TriggerSingleton instance = null;
	private FieldTestTrigger trigger;
	private long lastTriggerCheckTime = 0;
	
	private TriggerSingleton(FieldTestTrigger trigger) {
		this.trigger = trigger;
	}

	public static void init(FieldTestTrigger trigger) {
		synchronized (TriggerSingleton.class) {
			instance = new TriggerSingleton(trigger);
		}
	}
	
	public static TriggerSingleton getInstance() {
		synchronized (TriggerSingleton.class) {
			if(instance == null) {
				throw new IllegalStateException("Trigger singleton has not been initialized.");
			}
		}
		return instance;
	}

	
	public boolean checkTrigger() {
		long triggerStart = System.currentTimeMillis();
		boolean result = trigger.checkTrigger();
		lastTriggerCheckTime = System.currentTimeMillis() - triggerStart;
		return result;	
	}

	public long getLastTriggerCheckTime() {
		return lastTriggerCheckTime;
	}

	public List<String> getAdditionalTriggerInformation() {
		return trigger.additionalTriggerInformation();
	}

	public String getLastAdditionalTriggerInformation() {
		return trigger.lastAdditionalTriggerInformation();
	}
}
