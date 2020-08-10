package fieldtest.test;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.reflections.Reflections;

import fieldtest.logging.SingletonFieldTestLogger;
import fieldtest.statistics.SingletonStatisticsLogger;
import junit.framework.TestCase;

/**
 * A utility class for executing a field test for evaluation experiments. 
 * 
 * The class assumes a single JUnit test case in the package "fieldtest.test".
 * This is true for our experiments and allows to get rid of some boilerplate
 * code which could easily be extended. 
 *
 */
public class FieldTestRunner {
	public static Result runTests() {
		Logger logger = SingletonFieldTestLogger.getInstance();
		JUnitCore jUnitCore = new JUnitCore();
		Reflections reflections = new Reflections("fieldtest.test");
		Set<Class<? extends TestCase>> testClassesInPackage = reflections.getSubTypesOf(TestCase.class);
		
		// there should only be one test class usually
		if(testClassesInPackage.size() != 1) {
			logger.warning("There are " + testClassesInPackage.size() + " test suites. "
					+ "Cannot determine which one to run." );
			return null;
		}
		Class<? extends TestCase> singleTestClass = testClassesInPackage.iterator().next();
		
        Result result = jUnitCore.run(singleTestClass);
        
        logger.info("test class: " + singleTestClass.getSimpleName());        
        logger.info("ran: " + result.getRunCount() + " failed: " + result.getFailureCount());
        List<Failure> failures = result.getFailures();
        if(!failures.isEmpty()) {
            for(Failure f : failures) {
                if(f.getException() instanceof org.junit.AssumptionViolatedException || 
                		// for some reason we see the internal exception when we use the reflections library
                		f.getException() instanceof org.junit.internal.AssumptionViolatedException)
                  logger.info("Assumption violated");
                else {
                  logger.info("Error detected:");
                  logger.info(f.getTrace());
                }
            }
        }
		
        return result;
	}
}
