package fieldtest.aspect;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.apache.commons.lang3.RandomStringUtils;
import fieldtest.logging.SingletonFieldTestLogger;
import fieldtest.test.RandomStringUtilsTestField;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import fieldtest.test.FieldTestRunner;
import fieldtest.statistics.SingletonStatisticsLogger;
import fieldtest.triggering.TriggerSingleton;

public aspect RandomStringUtilsTester {
	
	pointcut callRandom(int count, int start, int end, boolean letters, boolean numbers, char[] chars, Random random): call(* RandomStringUtils.random(int, int, int, boolean, boolean, char[], Random)) && args(count, start, end, letters, numbers, chars, random) && if(!TestFlags.testing) && if(TestFlags.instrumentationON);
	
	before(int count, int start, int end, boolean letters, boolean numbers, char[] chars, Random random): callRandom(count, start, end, letters, numbers, chars, random){
		
		TestFlags.testing = true;
		
		TestStorage.count = count;
		TestStorage.start = start;
		TestStorage.end = end;
		TestStorage.letters = letters;
		TestStorage.numbers = numbers;
		TestStorage.chars = chars;
		TestStorage.random = random;
		
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
