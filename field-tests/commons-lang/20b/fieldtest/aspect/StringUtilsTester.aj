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
	
	pointcut callJoin(Object[] array, char separator, int startIndex, int endIndex): call(* StringUtils.join(Object[], char, int, int)) && args(array, separator, startIndex, endIndex) && if(!TestFlags.testing) && if(TestFlags.instrumentationON);
	
	before(Object[] array, char separator, int startIndex, int endIndex): callJoin(array, separator, startIndex, endIndex){
		
		TestFlags.testing = true;
        TestStorage.array = array;
		TestStorage.separator = separator;
		TestStorage.startIndex = startIndex;
		TestStorage.endIndex = endIndex;
		
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
