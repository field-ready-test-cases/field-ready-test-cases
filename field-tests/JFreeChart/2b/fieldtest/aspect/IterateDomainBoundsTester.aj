package fieldtest.aspect;

import java.util.List;
import java.util.logging.Logger;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import org.jfree.data.general.DatasetUtilities;
import fieldtest.test.DatasetUtilitiesTestField;
import org.jfree.data.xy.XYDataset;
import fieldtest.logging.SingletonFieldTestLogger;

import fieldtest.statistics.SingletonStatisticsLogger;
import fieldtest.triggering.TriggerSingleton;
import fieldtest.test.FieldTestRunner;

public aspect IterateDomainBoundsTester {
	
	pointcut callIterateDomainBounds(XYDataset dataset, boolean includeInterval): 
		call(* DatasetUtilities.iterateDomainBounds(XYDataset, boolean)) && 
		args (dataset, includeInterval) && if(!TestFlags.testing) && if(TestFlags.instrumentationON);
	
	before(XYDataset dataset, boolean includeInterval): callIterateDomainBounds(dataset, includeInterval){
		
		TestFlags.testing = true;
		
		TestStorage.dataset = dataset;
		
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
