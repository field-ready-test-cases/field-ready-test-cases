package fieldtest.aspect;

import java.util.List;
import java.util.logging.Logger;

import org.jfree.chart.util.ShapeList;
import fieldtest.logging.SingletonFieldTestLogger;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import fieldtest.statistics.SingletonStatisticsLogger;
import fieldtest.triggering.TriggerSingleton;
import fieldtest.test.FieldTestRunner;

public aspect ShapeListTester {
	
    // intercept size method to execute tests, TODO: intercept 
    // more methods 
	pointcut callShapeListSize(ShapeList shapeList): call(* ShapeList.size()) 
    && target(shapeList) && target(org.jfree.chart.util.ShapeList) && 
    if(!TestFlags.testing) && if(TestFlags.instrumentationON);
	
	before(ShapeList shapeList): callShapeListSize(shapeList){
		
		TestFlags.testing = true;
		
		TestStorage.shapeList = shapeList;
		
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
