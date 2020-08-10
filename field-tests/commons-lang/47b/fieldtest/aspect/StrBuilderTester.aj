package fieldtest.aspect;

import java.util.List;
import java.util.logging.Logger;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.apache.commons.lang.text.StrBuilder;

import fieldtest.logging.SingletonFieldTestLogger;
import fieldtest.test.StrBuilderTestField;

import fieldtest.test.FieldTestRunner;
import fieldtest.statistics.SingletonStatisticsLogger;
import fieldtest.triggering.TriggerSingleton;

public aspect StrBuilderTester {
	
	pointcut callAppend(StrBuilder sb, Object obj, int width, char padChar): call(* StrBuilder.appendFixedWidthPadRight(Object, int, char)) && args(obj, width, padChar) && target(sb) && if(!TestFlags.testing) && if(TestFlags.instrumentationON);
	
	before(StrBuilder sb,Object obj, int width, char padChar): callAppend(sb,obj, width, padChar){
		
		TestFlags.testing = true;
				
		TestStorage.obj = obj;
		TestStorage.sb = sb;
		TestStorage.width = width;
		TestStorage.padChar = padChar;
		
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
