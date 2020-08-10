package fieldtest.usage;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.runner.JUnitCore;

import fieldtest.logging.SingletonFieldTestLogger;
import fieldtest.statistics.ExperimentStatistics;
import fieldtest.statistics.SingletonStatisticsLogger;
import fieldtest.triggering.TriggerSingleton;
import fieldtest.triggering.composite.CompositeTrigger;
import fieldtest.triggering.grammar.GrammarBasedTrigger;
import fieldtest.triggering.simple.AllTestTrigger;
import fieldtest.triggering.simple.RandomTestTrigger;
import fieldtest.triggering.static_anomaly_detection.StaticAnomalyDetectionTrigger;


/** 
 * A class implementing a main method to initiate experiments with various 
 * triggering configurations. It assumes the existence of junit-quickcheck 
 * test suite in a class named <code>UseWithQuickCheck</code> which simulates 
 * the application under test using Quickcheck generator. 
 * Furthermore, it serializes measurement data from the experiments.
 */
public class UseDifferentTriggersWithQuickCheck {
	public static void main(String [] args) {
		int trainingLength = 1000;
		
		JUnitCore jUnitCore = new JUnitCore();
		String logBaseDirectory = System.getProperty("log-directory");
		int maxDepth = 3;
		boolean exploreArrays = true;
		boolean allowMultipleCoverage = false;
		TriggerSingleton.init(new StaticAnomalyDetectionTrigger(maxDepth, 
				exploreArrays,allowMultipleCoverage));	
		System.setProperty("log-directory", logBaseDirectory + "/" + "static-anomaly-no-dupl");
        jUnitCore.run(UseWithQuickCheck.class);
        writeExperimentLogs();
        SingletonStatisticsLogger.getInstance().getExperimentStatistics().clear();
        SingletonFieldTestLogger.reset();
        
        TriggerSingleton.init(new AllTestTrigger());	
		System.setProperty("log-directory", logBaseDirectory + "/" + "all");
		jUnitCore.run(UseWithQuickCheck.class);
		writeExperimentLogs();
        SingletonStatisticsLogger.getInstance().getExperimentStatistics().clear();
        SingletonFieldTestLogger.reset();
        
        double triggerProbability = 0.05;
		TriggerSingleton.init(new RandomTestTrigger(triggerProbability));	
		System.setProperty("log-directory", logBaseDirectory + "/" + "random-0_05");
		jUnitCore.run(UseWithQuickCheck.class);
		writeExperimentLogs();
        SingletonStatisticsLogger.getInstance().getExperimentStatistics().clear();
        SingletonFieldTestLogger.reset();
                
        exploreArrays = false;
        GrammarBasedTrigger grammarBasedTrigger = new GrammarBasedTrigger(trainingLength,maxDepth, 
				exploreArrays);
        TriggerSingleton.init(grammarBasedTrigger);	
		System.setProperty("log-directory", logBaseDirectory + "/" + "grammar-based-training");
        jUnitCore.run(UseWithQuickCheck.class);
        writeExperimentLogs();
        SingletonStatisticsLogger.getInstance().getExperimentStatistics().clear();
        SingletonFieldTestLogger.reset();
        grammarBasedTrigger.clearCoverageData();
        	
		System.setProperty("log-directory", logBaseDirectory + "/" + "grammar-based");
        jUnitCore.run(UseWithQuickCheck.class);
        writeExperimentLogs();
        SingletonStatisticsLogger.getInstance().getExperimentStatistics().clear();
        SingletonFieldTestLogger.reset();
        grammarBasedTrigger.clearCoverageData();
        
        System.setProperty("log-directory", logBaseDirectory + "/" + "composite");
        CompositeTrigger compositeTrigger = new CompositeTrigger();
		triggerProbability = 0.05;
		RandomTestTrigger componentRandomTrigger = 
				new RandomTestTrigger(triggerProbability);	
		maxDepth = 3;
		exploreArrays = true;
		allowMultipleCoverage = false;
		StaticAnomalyDetectionTrigger componentStaticDetector = new StaticAnomalyDetectionTrigger(maxDepth, 
				exploreArrays,allowMultipleCoverage);
		compositeTrigger.addTrigger(componentStaticDetector);
		compositeTrigger.addTrigger(grammarBasedTrigger);
		compositeTrigger.addTrigger(componentRandomTrigger);
		TriggerSingleton.init(compositeTrigger);
		jUnitCore.run(UseWithQuickCheck.class);
        writeExperimentLogs();
        SingletonStatisticsLogger.getInstance().getExperimentStatistics().clear();
        SingletonFieldTestLogger.reset();
        grammarBasedTrigger.clearCoverageData();
        
        System.setProperty("log-directory", logBaseDirectory + "/" + "composite-no-random");
        CompositeTrigger compositeNoRandTrigger = new CompositeTrigger();
		maxDepth = 3;
		exploreArrays = true;
		allowMultipleCoverage = false;
		StaticAnomalyDetectionTrigger componentNoRandomStaticDetector = 
				new StaticAnomalyDetectionTrigger(maxDepth, 
				exploreArrays,allowMultipleCoverage);
		compositeNoRandTrigger.addTrigger(componentNoRandomStaticDetector);
		compositeNoRandTrigger.addTrigger(grammarBasedTrigger);
		TriggerSingleton.init(compositeNoRandTrigger);
		jUnitCore.run(UseWithQuickCheck.class);
        writeExperimentLogs();
        SingletonStatisticsLogger.getInstance().getExperimentStatistics().clear();
        SingletonFieldTestLogger.reset();
        grammarBasedTrigger.clearCoverageData();
	}
	public static void writeExperimentLogs() {
		ExperimentStatistics experimentStats = 
				SingletonStatisticsLogger.getInstance().getExperimentStatistics();
		experimentStats.computeSummary();
		Marshaller m;
		// Write to File
		File target;

		String fieldTestId = System.getProperty("field-test-id");
        String logDirectory = System.getProperty("log-directory");
		 if(fieldTestId == null || logDirectory == null){
			 System.err.println("Cannot log to file");  
			 return;
         }
         else{
             // This block configure the logger with handler and formatter
             new File(logDirectory).mkdirs();
             target = new File(logDirectory + "/test_statistics" + 
                  fieldTestId + ".xml");
         }
		try {
			JAXBContext statXMLContext = JAXBContext.newInstance(ExperimentStatistics.class);
			m = statXMLContext.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			if (!target.getParentFile().exists())
				target.getParentFile().mkdirs();
			m.marshal(experimentStats, target);
		} catch (JAXBException e) {
			System.err.println("Could not write XML experiment statistics");
		}
	}
}
