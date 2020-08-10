package fieldtest.aspect;

import java.util.List;
import java.util.logging.Logger;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.apache.commons.lang3.StringUtils;

import fieldtest.logging.SingletonFieldTestLogger;
import fieldtest.test.StringUtilsTestField;


import fieldtest.test.FieldTestRunner;
import fieldtest.statistics.SingletonStatisticsLogger;
import fieldtest.triggering.TriggerSingleton;

public aspect StringUtilsTester {
	
    pointcut callEquals(CharSequence cs1, CharSequence cs2): call(* StringUtils.equals(CharSequence, CharSequence)) && args(cs1, cs2) && if(!TestFlags.testing) && if(TestFlags.instrumentationON);

	before(CharSequence cs1, CharSequence cs2): callEquals(cs1, cs2){
			
		TestFlags.testing = true;
		TestStorage.cs1 = cs1;
		TestStorage.cs2 = cs2;
		
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
