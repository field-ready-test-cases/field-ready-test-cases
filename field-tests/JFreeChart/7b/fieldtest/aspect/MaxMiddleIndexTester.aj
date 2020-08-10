package fieldtest.aspect;

import java.util.List;
import java.util.logging.Logger;

import org.jfree.chart.util.ShapeList;
import org.jfree.data.time.TimePeriodValues;
import fieldtest.logging.SingletonFieldTestLogger;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import fieldtest.statistics.SingletonStatisticsLogger;
import fieldtest.triggering.TriggerSingleton;
import fieldtest.test.FieldTestRunner;

public aspect MaxMiddleIndexTester {
	
	pointcut callGetItemCount(TimePeriodValues tpv): call(* TimePeriodValues.getItemCount()) && target(tpv) && if(!TestFlags.testing) && if(TestFlags.instrumentationON);
	
	after(TimePeriodValues tpv): callGetItemCount(tpv){
		
		TestFlags.testing = true;
		
		TestStorage.tpv = tpv;
		
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
