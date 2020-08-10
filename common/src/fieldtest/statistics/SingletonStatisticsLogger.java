package fieldtest.statistics;

import org.junit.runner.Result;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * A singleton class for logging structured data about field-test executions.
 * 
 */
public class SingletonStatisticsLogger {
	 private static SingletonStatisticsLogger instance;
	 private ExperimentStatistics experimentStatistics = null;
	 private SingletonStatisticsLogger() {
		 experimentStatistics = new ExperimentStatistics();
	 }
	 
    public static SingletonStatisticsLogger getInstance() {
        synchronized (SingletonStatisticsLogger.class) {
            if (instance == null) {
            	instance = new SingletonStatisticsLogger();
            }
        }
        return instance;
    }

	public void log(boolean triggered, Result result, long triggerCheckTime, long testTime) {
		experimentStatistics.log(triggered, result,triggerCheckTime, testTime);
	}

	public ExperimentStatistics getExperimentStatistics() {
		return experimentStatistics;
	}

	public void setExperimentStatistics(ExperimentStatistics experimentStatistics) {
		this.experimentStatistics = experimentStatistics;
	}
	


}