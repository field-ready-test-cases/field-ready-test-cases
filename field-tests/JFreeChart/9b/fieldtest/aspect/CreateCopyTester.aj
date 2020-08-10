package fieldtest.aspect;

import java.util.List;
import java.util.logging.Logger;

import org.jfree.data.time.Day;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import fieldtest.logging.SingletonFieldTestLogger;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import fieldtest.statistics.SingletonStatisticsLogger;
import fieldtest.triggering.TriggerSingleton;
import fieldtest.test.FieldTestRunner;

public aspect CreateCopyTester {
	
	pointcut callCreateCopy(TimeSeries series, RegularTimePeriod start, RegularTimePeriod end): call(* TimeSeries.createCopy(RegularTimePeriod, RegularTimePeriod)) && args (start, end) && target(series) && if(!TestFlags.testing) && if(TestFlags.instrumentationON);
	
	before(TimeSeries series, RegularTimePeriod start, RegularTimePeriod end): callCreateCopy(series, start, end){
		
		TestFlags.testing = true;
		
		TestStorage.series = series;
		TestStorage.start = start;
		TestStorage.end = end;
		
		Logger logger = SingletonFieldTestLogger.getInstance();
		
		try {
			TriggerSingleton triggerChecker = TriggerSingleton.getInstance();
	        if(triggerChecker.checkTrigger()) {
	        	long testStartTime = System.currentTimeMillis();
				Result result = FieldTestRunner.runTests();
				long testTime = System.currentTimeMillis() - testStartTime;
	        	SingletonStatisticsLogger.getInstance().log(true, result, 
	        			triggerChecker.getLastTriggerCheckTime(),testTime);
	        } else {
	        	SingletonStatisticsLogger.getInstance().log(false,null, 
	        			triggerChecker.getLastTriggerCheckTime(),0l);
	        }
        } catch(Exception e) {
        	logger.warning("Exception in non-field-test code: "  + e.getMessage());
        }
        
        TestFlags.testing = false;
		
	}

}
