package fieldtest.statistics;

import org.junit.runner.Result;

/**
 * 
 * A class storing information about a field-test case (non-)activation. 
 * Whenever the AspectJ advice corresponding to a field test is executed, an 
 * object of this class is created and in an objection of the class 
 * <code>ExperimentStatistics</code>.
 *
 * The <code>Verdict</code> enum allows to distinguish four cases:
 *  (1) <code>NOT_TRIGGERED</code> signals that the field test was not 
 *  triggered,
 *  (2) <code>ASSUMPTION_VIOLATION</code> signals that the field test's 
 *  preconditions were violated,
 *  (3) <code>FAIL</code> signals that the field test failed,
 *  and (4) <code>PASS</code> signals that the field test passed.
 *  
 */
public class SingleCallStatistics {
	public static enum TestResult{
		NOT_TRIGGERED,FAIL, ASSUMPTION_VIOLATION,PASS
	}
	private TestResult result = null;
	private FailureDetails additionalDetails;
	private String additionalTriggerDetails = null;
	private long testTime;
	private long triggerCheckTime;

	public SingleCallStatistics() {
		this(TestResult.NOT_TRIGGERED,0, 0, null);
	}
	public TestResult getResult() {
		return result;
	}

	public void setResult(TestResult result) {
		this.result = result;
	}

	public FailureDetails getAdditionalDetails() {
		return additionalDetails;
	}

	public void setAdditionalDetails(FailureDetails additionalDetails) {
		this.additionalDetails = additionalDetails;
	}

	public long getTestTime() {
		return testTime;
	}

	public void setTestTime(long testTime) {
		this.testTime = testTime;
	}

	public long getTriggerCheckTime() {
		return triggerCheckTime;
	}

	public void setTriggerCheckTime(long triggerCheckTime) {
		this.triggerCheckTime = triggerCheckTime;
	}

	public SingleCallStatistics(TestResult result, long triggerCheckTime, 
			long testTime) {
		this(result,triggerCheckTime, testTime, null);
	}

	public SingleCallStatistics(TestResult result, long triggerCheckTime, 
			long testTime,FailureDetails additionalDetails) {
		this.result = result;
		this.additionalDetails = additionalDetails;
		this.triggerCheckTime = triggerCheckTime;
		this.testTime=testTime;
	}
	public String getAdditionalTriggerDetails() {
		return additionalTriggerDetails;
	}
	public void setAdditionalTriggerDetails(String additionalTriggerDetails) {
		this.additionalTriggerDetails = additionalTriggerDetails;
	}
}
