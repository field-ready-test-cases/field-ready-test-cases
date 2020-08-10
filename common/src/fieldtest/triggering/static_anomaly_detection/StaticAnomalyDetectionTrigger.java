package fieldtest.triggering.static_anomaly_detection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.junit.runner.Result;

import java.util.Set;

import fieldtest.logging.SingletonFieldTestLogger;
import fieldtest.test.FieldTestRunner;
import fieldtest.triggering.FieldTestTrigger;

/**
 * The implementation of the triggering mechanism called static anomaly 
 * detection in the paper. It uses 
 * <code>DefaultStaticDetectionTreeTraverser</code> to explore the test data
 * compare against special values associated with faults. The trigger can be 
 * configured with an exploration data and the options to explore arrays
 * and to trigger multiple for the same value. 
 * The additional information provided by the trigger includes the covered 
 * values used for triggering. 
 */
public class StaticAnomalyDetectionTrigger implements FieldTestTrigger{

	private DefaultStaticDetectionTreeTraverser traverser = null;
	int trainingLength = 0;
	
	public StaticAnomalyDetectionTrigger(int maxDepth, boolean exploreArrays,
			boolean allowMultipleCoverage) {
		this.traverser = new DefaultStaticDetectionTreeTraverser(maxDepth, 
				exploreArrays, false,allowMultipleCoverage);
	}
	public StaticAnomalyDetectionTrigger(int maxDepth, boolean exploreArrays,
			boolean allowMultipleCoverage, int trainingLength) {
		this(maxDepth, exploreArrays, allowMultipleCoverage);
		this.trainingLength = trainingLength;
	}
	@Override
	public boolean checkTrigger() {
		if(trainingLength > 0) {
			// run tests here in training phase (this is only for evaluation)
			Result result = FieldTestRunner.runTests();
			if(result.wasSuccessful()) {
				traverser.traverseTree();
			}
			trainingLength --;
		
			if(trainingLength <= 0) {
				traverser.resetFoundSuspiciousValue();
				SingletonFieldTestLogger.getInstance().info("Training phase is over now");
			}
			return false;
		} else {
			traverser.traverseTree();
			boolean coveredNewSuspiciousValue = traverser.hasFoundSuspiciousValue();
			traverser.resetFoundSuspiciousValue();
			return coveredNewSuspiciousValue;
		}
	}
	@Override
	public List<String> additionalTriggerInformation() {
		List<String> coveredElementsList = new ArrayList<>();
		for (Entry<List<String>, Set<String>> coveredElement : 
			traverser.getCoveredPathToElement().entrySet()) {
			String path = String.join("->", coveredElement.getKey());
			String coveredElementsAfterPath = String.join(",", coveredElement.getValue());
			coveredElementsList.add("covered: " + path + " : " + coveredElementsAfterPath);
		}
		return coveredElementsList;
	}
	@Override
	public String lastAdditionalTriggerInformation() {
		if(traverser.getLastFoundValue() != null) {
			String path = String.join("->", traverser.getLastFoundValue().getKey());
			return "covered: " + path + " : " + traverser.getLastFoundValue().getValue();
		}	else {
			return null;
		}
	}

}
