package fieldtest.aspect;

import java.util.List;
import java.util.logging.Logger;
import java.util.Map;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.apache.commons.lang.text.ExtendedMessageFormat;

import fieldtest.logging.SingletonFieldTestLogger;
import fieldtest.test.ExtendedMessageFormatTestField;

import fieldtest.test.FieldTestRunner;
import fieldtest.statistics.SingletonStatisticsLogger;
import fieldtest.triggering.TriggerSingleton;

public aspect ExtendedMessageFormatTester {
	
	pointcut callFormat(String pattern, Map registry): initialization(ExtendedMessageFormat.new(String, Map)) && args(pattern, registry) && if(!TestFlags.testing) && if(TestFlags.instrumentationON);
	
	before(String pattern, Map registry): callFormat(pattern, registry){
		
		TestFlags.testing = true;
				
		TestStorage.pattern = pattern;
		TestStorage.registry = registry;
		
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
