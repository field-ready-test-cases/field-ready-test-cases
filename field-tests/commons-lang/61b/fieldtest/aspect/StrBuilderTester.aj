package fieldtest.aspect;

import java.util.List;
import java.util.logging.Logger;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import fieldtest.logging.SingletonFieldTestLogger;
import fieldtest.test.StrBuilderTestField;
import org.apache.commons.lang.text.StrBuilder;

import fieldtest.test.FieldTestRunner;
import fieldtest.statistics.SingletonStatisticsLogger;
import fieldtest.triggering.TriggerSingleton;

public aspect StrBuilderTester {
	
	pointcut callDelete(String s, StrBuilder sb): call(* StrBuilder.deleteAll(String)) && args(s) && target(sb) && if(!TestFlags.testing) && if(TestFlags.instrumentationON);
	
	before(String s, StrBuilder sb): callDelete(s, sb){
		
		TestFlags.testing = true;
				
		TestStorage.s = s;
		TestStorage.sb = sb;
		
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
