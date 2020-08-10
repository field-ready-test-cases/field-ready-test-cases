package fieldtest.aspect;

import java.util.List;
import java.util.logging.Logger;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import fieldtest.logging.SingletonFieldTestLogger;
import org.apache.commons.lang3.StringUtils;
import fieldtest.test.StringUtilsTestField;

import fieldtest.test.FieldTestRunner;
import fieldtest.statistics.SingletonStatisticsLogger;
import fieldtest.triggering.TriggerSingleton;

public aspect StringUtilsTester {
	
	pointcut callReplace(String text, String[] searchList, String[] replacementList): call(* StringUtils.replaceEach(String, String[], String[])) && args(text, searchList, replacementList) && if(!TestFlags.testing) && if(TestFlags.instrumentationON);
	
	before(String text, String[] searchList, String[] replacementList): callReplace(text, searchList, replacementList){
		
		TestFlags.testing = true;
				
		TestStorage.text = text;
		TestStorage.searchList = searchList;
		TestStorage.replacementList = replacementList;
		
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
