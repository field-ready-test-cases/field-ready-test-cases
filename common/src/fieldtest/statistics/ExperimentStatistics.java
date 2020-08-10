package fieldtest.statistics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.omg.CORBA.portable.IndirectionException;

import fieldtest.logging.SingletonFieldTestLogger;
import fieldtest.statistics.SingleCallStatistics.TestResult;
import fieldtest.triggering.TriggerSingleton;

/**
 * A class for storing data about a field-testing experiment. 
 * 
 * It stores a list of data about individual field-test executions and provides
 * a method for computing a summary of the data. Objects can be conveniently
 * (de-)serialized with JAXB. 
 *
 */
@XmlRootElement(name = "experiment-statistics")
public class ExperimentStatistics {
	public static class ExperimentSummary{
		private double averageTriggerCheckTime;
		private double averageTestTime;
		private double ratioOfExecutedTests;
		private double ratioFailToPass;
		// try to count unique failure by checking the additional failure details
		private int probablyUniqueFailures;
		private int nrExecutedTests;
		private int nrPotentiallyExecutedTests;
		private List<String> additionalTriggerInfo;
		
		
		public ExperimentSummary(double averageTriggerCheckTime, double averageTestTime, double ratioOfExecutedTests,
				double ratioFailToPass, int probablyUniqueFailures, int nrExecutedTests,
				int nrPotentiallyExecutedTests) {
			super();
			this.averageTriggerCheckTime = averageTriggerCheckTime;
			this.averageTestTime = averageTestTime;
			this.ratioOfExecutedTests = ratioOfExecutedTests;
			this.ratioFailToPass = ratioFailToPass;
			this.probablyUniqueFailures = probablyUniqueFailures;
			this.nrExecutedTests = nrExecutedTests;
			this.nrPotentiallyExecutedTests = nrPotentiallyExecutedTests;
		}
		public ExperimentSummary() {
			super();
			this.averageTriggerCheckTime = 0;
			this.averageTestTime = 0;
			this.ratioOfExecutedTests = 0;
			this.ratioFailToPass = 0;
			this.probablyUniqueFailures = 0;
			this.nrExecutedTests = 0;
			this.nrPotentiallyExecutedTests = 0;
		}
		public double getAverageTriggerCheckTime() {
			return averageTriggerCheckTime;
		}
		public void setAverageTriggerCheckTime(double averageTriggerCheckTime) {
			this.averageTriggerCheckTime = averageTriggerCheckTime;
		}
		public double getAverageTestTime() {
			return averageTestTime;
		}
		public void setAverageTestTime(double averageTestTime) {
			this.averageTestTime = averageTestTime;
		}
		public double getRatioOfExecutedTests() {
			return ratioOfExecutedTests;
		}
		public void setRatioOfExecutedTests(double ratioOfExecutedTests) {
			this.ratioOfExecutedTests = ratioOfExecutedTests;
		}
		public double getRatioFailToPass() {
			return ratioFailToPass;
		}
		public void setRatioFailToPass(double ratioFailToPass) {
			this.ratioFailToPass = ratioFailToPass;
		}
		public int getProbablyUniqueFailures() {
			return probablyUniqueFailures;
		}
		public void setProbablyUniqueFailures(int probablyUniqueFailures) {
			this.probablyUniqueFailures = probablyUniqueFailures;
		}
		public int getNrPotentiallyExecutedTests() {
			return nrPotentiallyExecutedTests;
		}
		public void setNrPotentiallyExecutedTests(int nrPotentiallyExecutedTests) {
			this.nrPotentiallyExecutedTests = nrPotentiallyExecutedTests;
		}
		public int getNrExecutedTests() {
			return nrExecutedTests;
		}
		public void setNrExecutedTests(int nrExecutedTests) {
			this.nrExecutedTests = nrExecutedTests;
		}
		public List<String> getAdditionalTriggerInfo() {
			return additionalTriggerInfo;
		}
		public void setAdditionalTriggerInfo(List<String> additionalTriggerInfo) {
			this.additionalTriggerInfo = additionalTriggerInfo;
		}
		
	}
	private List<SingleCallStatistics> callStatistics = new ArrayList<>();
	private ExperimentSummary summary = null;
	
	public void log(boolean triggered, Result result, long triggerCheckTime, long testTime) {
		if(triggered) {
			SingleCallStatistics singleCallStats = null;
			if(result.getRunCount() > 1) {
				SingletonFieldTestLogger.getInstance().warning(
						"Something went wrong, test suite should contain only a single test case");
			} else {
				if(result.getFailureCount() == 0) {
					singleCallStats = 
							new SingleCallStatistics(TestResult.PASS,triggerCheckTime, testTime);
				} else {
					// there is only one failure by design
					Failure failure = result.getFailures().get(0); 
					if (failure.getException() instanceof org.junit.AssumptionViolatedException){
						singleCallStats = 
								new SingleCallStatistics(
										TestResult.ASSUMPTION_VIOLATION,triggerCheckTime, testTime);
					} else {
						FailureDetails additionalFailureDetails = new FailureDetails(failure.getDescription().toString(),
						failure.getException().getMessage(),
						failure.getException().getClass().getCanonicalName(),
						failure.getTrace());
						singleCallStats = new SingleCallStatistics(TestResult.FAIL,triggerCheckTime, 
								testTime,additionalFailureDetails);
						
					}
				}
			}
			String additionalTriggerDetails = TriggerSingleton.getInstance()
					.getLastAdditionalTriggerInformation();
			singleCallStats.setAdditionalTriggerDetails(additionalTriggerDetails);
			getCallStatistics().add(singleCallStats);
		} else {
			getCallStatistics().add(new SingleCallStatistics(TestResult.NOT_TRIGGERED, triggerCheckTime, testTime));
		}
		
	}

	public List<SingleCallStatistics> getCallStatistics() {
		return callStatistics;
	}

	public void setCallStatistics(List<SingleCallStatistics> callStatistics) {
		this.callStatistics = callStatistics;
	}
	public void computeSummary(){
		double averageTriggerCheckTime = 0;
		double averageTestTime = 0;
		int probablyUniqueFailures = 0;
		double ratioOfExecutedTests = 0;
		int failed = 0;
		int passed = 0;
		int executedTests = 0;
		Set<String> failureDetailsSummaries= new HashSet<>();

		for (SingleCallStatistics individualStatistics : this.callStatistics) {
			averageTriggerCheckTime += individualStatistics.getTriggerCheckTime();
			if(!individualStatistics.getResult().equals(TestResult.NOT_TRIGGERED)) {
				averageTestTime += individualStatistics.getTestTime();
				executedTests ++;
				if(individualStatistics.getResult().equals(TestResult.FAIL)) {
					failed++;
					String additionalDetailsString = 
						individualStatistics.getAdditionalDetails().toString();
					if(!failureDetailsSummaries.contains(additionalDetailsString)) {
						probablyUniqueFailures++;
						failureDetailsSummaries.add(additionalDetailsString);
					}
				}
				else if(individualStatistics.getResult().equals(TestResult.PASS))
					passed++;
					
			}
		}
		
		averageTriggerCheckTime /= callStatistics.size();
		if(executedTests != 0)
			averageTestTime /= executedTests;
		else 
			averageTestTime = 0;
		ratioOfExecutedTests = ((double) executedTests) / callStatistics.size();
				
		double ratioFailToPass = failed == 0 && passed == 0 ? 1 : passed == 0 ? 
				Double.POSITIVE_INFINITY : (double) failed/passed;
		
		summary = new ExperimentSummary(averageTriggerCheckTime, averageTestTime, 
				ratioOfExecutedTests, ratioFailToPass, probablyUniqueFailures,
				executedTests,callStatistics.size());
		
		
		summary.setAdditionalTriggerInfo(TriggerSingleton.getInstance().getAdditionalTriggerInformation());
	}

	public ExperimentSummary getSummary() {
		return summary;
	}

	public void setSummary(ExperimentSummary summary) {
		this.summary = summary;
	}
	
	public void clear() {
		this.summary = null;
		this.callStatistics.clear();
	}

}
