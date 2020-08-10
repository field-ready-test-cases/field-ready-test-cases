package fieldtest.aspect;

import java.awt.geom.GeneralPath;
import java.util.List;
import java.util.logging.Logger;

import org.jfree.chart.util.ShapeUtilities;

import fieldtest.logging.SingletonFieldTestLogger;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import fieldtest.statistics.SingletonStatisticsLogger;
import fieldtest.triggering.TriggerSingleton;
import fieldtest.test.FieldTestRunner;

public aspect ShapeUtilitiesEqualTester {
	
	pointcut callShapeUtilitiesEquals(GeneralPath p1, GeneralPath p2): call(* ShapeUtilities.equal(GeneralPath, GeneralPath)) && args (p1,p2) && if(!TestFlags.testing) && if(TestFlags.instrumentationON);
	
	before(GeneralPath p1, GeneralPath p2): callShapeUtilitiesEquals(p1, p2){
		
		TestFlags.testing = true;
		
		TestStorage.p1 = p1;
		TestStorage.p2 = p2;
		
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
