package fieldtest.triggering.grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.junit.runner.Result;

import fieldtest.logging.SingletonFieldTestLogger;
import fieldtest.test.FieldTestRunner;
import fieldtest.triggering.FieldTestTrigger;
import fieldtest.triggering.grammar.alergia.Alergia;
import fieldtest.triggering.grammar.alergia.MarkovChain;
import fieldtest.triggering.grammar.alergia.McState;
import fieldtest.triggering.grammar.alergia.Symbol;
import fieldtest.triggering.grammar.alergia.dotexport.DotExporter;
/**
 * A triggering mechanism based on grammar mining. It uses a training phase of 
 * length <code>trainingPhaseLength</code> to store string data for mining a
 * grammar in the form of a Markov chain. 
 * 
 * The Markov chain is used to parse string data during field operation. In 
 * case the probability of producing a specific string with a mined Markov 
 * chain is low, a field-test execution is triggered.
 * 
 * Whenever a field-test execution is triggered, this trigger stores the 
 * coverage data on the Markov chain obtained during parsing. This serves avoid
 * triggering for similar strings multiple times. 
 * The additional information provided by the trigger contains the mined
 * Markov chains.
 *
 */
public class GrammarBasedTrigger implements FieldTestTrigger {
	
	private GrammarCheckTestParameterTraverser traverser;
	private int trainingPhaseLength = 0;

	public GrammarBasedTrigger(int trainingPhaseLength, int maxDepth,boolean exploreArrays) {
		this.traverser = new GrammarCheckTestParameterTraverser(maxDepth, exploreArrays);
		this.trainingPhaseLength = trainingPhaseLength;
	}
	public void clearCoverageData() {
		traverser.clearCoverageData();
	}
	
	@Override
	public boolean checkTrigger() {

		if(trainingPhaseLength > 0) {
//					 run tests here in training phase (this is only for evaluation)
			Result result = FieldTestRunner.runTests();
			if(result.wasSuccessful()) {
				traverser.traverseTree();
			}
			trainingPhaseLength --;

			if(trainingPhaseLength <= 0) {
				traverser.resetFoundSuspiciousValue();
				traverser.stopTraining();
				SingletonFieldTestLogger.getInstance().info("Training phase is over now");
			}
			return false;
		} else {				
			traverser.resetFoundSuspiciousValue();
			traverser.traverseTree();
			boolean coveredNewSuspiciousValue = traverser.hasFoundSuspiciousValue();
			traverser.resetFoundSuspiciousValue();
			return coveredNewSuspiciousValue;
		}
	}

	@Override
	public List<String> additionalTriggerInformation() {
		Map<List<String>, MarkovChain> minedGrammars = traverser.getPathToMinedGrammar();
		DotExporter dotExporter = new DotExporter();
		ArrayList<String> additionalInfo = new ArrayList<String>();
		for(Entry<List<String>, MarkovChain> grammarForPath : minedGrammars.entrySet()) {
			StringBuilder infoForGrammar = new StringBuilder();
			infoForGrammar.append("Path: ");
			for(String pathElem : grammarForPath.getKey()) {
				infoForGrammar.append(pathElem);
				infoForGrammar.append(',');
			}
			infoForGrammar.append(" MC:");
			infoForGrammar.append(System.lineSeparator());
			infoForGrammar.append(dotExporter.toDot(grammarForPath.getValue()));
			additionalInfo.add(infoForGrammar.toString());
		}
		return additionalInfo;
	}

	@Override
	public String lastAdditionalTriggerInformation() {
		return null;
	}

}
