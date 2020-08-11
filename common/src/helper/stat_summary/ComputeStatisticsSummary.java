package helper.stat_summary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import fieldtest.statistics.ExperimentStatistics;
import fieldtest.statistics.SingleCallStatistics;
import fieldtest.statistics.SingleCallStatistics.TestResult;

/** 
 * A utility class for computing a summary of the field-test experiment data.
 *
 */
public class ComputeStatisticsSummary {

	
	public static FilenameFilter xmlFilter = new FilenameFilter() {
		
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith("b.xml");
		}
	};
	static boolean excludeLang43 = true;
	public static void main(String[] args) {

        try{
            JAXBContext statXMLContext = JAXBContext.newInstance(ExperimentStatistics.class);
            Unmarshaller um = statXMLContext.createUnmarshaller();
            List<File> rootDirFiles = new ArrayList<File>();
            String experimentLoc = System.getProperty("experimentloc");
            if(experimentLoc == null){
                System.out.println("Experiment location not specified");
                return;
            }
            String rootDir1 = experimentLoc + "/lang_logs/";
            File rootDirFile1 = new File(rootDir1);
            rootDirFiles.add(rootDirFile1);
            
            String rootDir2 = experimentLoc + "/chart_logs/";
            File rootDirFile2 = new File(rootDir2);
            rootDirFiles.add(rootDirFile2);
            
            
            List<File> childFiles = new ArrayList<File>();
            for(File rootDirFile : rootDirFiles){
                childFiles.addAll(Arrays.asList(rootDirFile.listFiles()));
            }
            
            Map<String,List<File>> configToLogs = new HashMap<>();
            for(File child : childFiles) {
                if(child.isDirectory()) {
                    String configName = child.getName();
                    List<File> logs = configToLogs.get(configName);
                    if(logs == null) {
                        logs = new ArrayList<File>();
                        configToLogs.put(configName, logs);
                    }
                    File[] experimentLogs = child.listFiles(xmlFilter);
                    for(File experimentLog : experimentLogs) {
                        logs.add(experimentLog);
                    }
                }
            }
            
            Map<String,Summary> configToSummaries = new HashMap<>();
            for(String configName : configToLogs.keySet()) {
    //			if(child.isDirectory()) {
    //				String configName = child.getName();
                List<File> experimentLogs = configToLogs.get(configName);
                Summary summary = new Summary();
                for(File experimentLog : experimentLogs) {
                    ExperimentStatistics stats = (ExperimentStatistics) um.unmarshal(
                            new FileReader(experimentLog));
                    summary.averageNrExecutedTests += stats.getSummary().getNrExecutedTests();
                    int nrFailing = 0;
                    for(SingleCallStatistics cs : stats.getCallStatistics()) {
                        if(cs.getResult().equals(TestResult.FAIL))
                                nrFailing++;
                    }
                    if(nrFailing > 0) {
                        summary.bugsDetected += experimentLog.getName();
                        summary.bugsDetected += ',';
                        summary.nrBugsDetected ++;
                    }
                    double percentageFailing = 0;
                    if(stats.getSummary().getNrExecutedTests() > 0) {
                            percentageFailing = 100.0* ((double)nrFailing / 
                            stats.getSummary().getNrExecutedTests());
                    }
                    summary.averagePercentageFailing += percentageFailing;
                    
                    if(!(excludeLang43 && experimentLog.getName().contains("lang-43"))) {
                        summary.averageTestTime += stats.getSummary().getAverageTestTime();
                        summary.averageTriggerCheckTime 
                        += stats.getSummary().getAverageTriggerCheckTime();
                    }
                    
                }
                summary.averageNrExecutedTests /= experimentLogs.size();
                summary.averagePercentageFailing /= experimentLogs.size();
                if(excludeLang43) {
                    // minus one for lang-43
                    summary.averageTestTime /= (experimentLogs.size()-1);
                    summary.averageTriggerCheckTime /= (experimentLogs.size()-1);
                } {
                    summary.averageTestTime /= (experimentLogs.size());
                    summary.averageTriggerCheckTime /= (experimentLogs.size());
                }
                configToSummaries.put(configName, summary);
    //			}
            }

            Summary allSummary = configToSummaries.get("all");
            for(String configName : configToSummaries.keySet()) {
                Summary summary = configToSummaries.get(configName);

                System.out.println("Summary for config:      " + configName);
                System.out.println("Avg. #exec. tests:       " + roundDouble(
                        100* ((double)summary.averageNrExecutedTests / allSummary.averageNrExecutedTests)));
                System.out.println("Avg. #exec. tests (abs.):" + roundDouble(
                        ((double)summary.averageNrExecutedTests)));
                System.out.println("#bugs detected:          " + roundDouble(
                        100* ((double)summary.nrBugsDetected / allSummary.nrBugsDetected)));
                System.out.println("#bugs detected (abs.):   " +  summary.nrBugsDetected);
                System.out.println("Avg. tigger check time:  " + roundDouble(summary.averageTriggerCheckTime));
                System.out.println("Avg. test time:          " + roundDouble(summary.averageTestTime));
            }
        } catch(FileNotFoundException e){
            System.out.println("Caught FileNotFoundException: " + e.getMessage());
        } catch(JAXBException  e){
            System.out.println("Caught JAXBException: " + e.getMessage());
        }
	}
	
	public static String roundDouble(double value) {
		return String.format("%.3f", value);
	}

}
