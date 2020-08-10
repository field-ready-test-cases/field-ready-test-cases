package fieldtest.aspect;

import java.util.List;
import java.util.logging.Logger;
import org.jfree.data.xy.XYSeries;
import fieldtest.logging.SingletonFieldTestLogger;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


import fieldtest.statistics.SingletonStatisticsLogger;
import fieldtest.triggering.TriggerSingleton;
import fieldtest.test.FieldTestRunner;

public aspect XYSeriesTester {
	
    // produces a warning about not matching, but matches anyway
	pointcut callGetItemCount(XYSeries series): call(* XYSeries.getItemCount()) && target(series) &&  target(org.jfree.data.xy.XYSeries) && if(!TestFlags.testing) && if(TestFlags.instrumentationON);
	
	before(XYSeries series): callGetItemCount(series){
		
		TestFlags.testing = true;
		
		TestStorage.series = series;
		
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
