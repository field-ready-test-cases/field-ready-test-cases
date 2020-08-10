package fieldtest.triggering.grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import fieldtest.logging.SingletonFieldTestLogger;
import fieldtest.triggering.grammar.alergia.Alergia;
import fieldtest.triggering.grammar.alergia.MarkovChain;
import fieldtest.triggering.grammar.alergia.McState;
import fieldtest.triggering.grammar.alergia.Symbol;
import fieldtest.triggering.parameter_tree.ImmutableList;
import fieldtest.triggering.parameter_tree.TestParameterTreeTraverser;
/**
 * This class searches for strings in the field test data stored in the class 
 * <code>TestStorage</code>. In the training phase, it simply stores the 
 * strings and mines a Markov chain to generalize from them at the end of the
 * training phase. 
 * 
 * During field operation, it uses the mined Markov chains to parse the strings
 * in the field test data. The parsing serves to check whether the strings in 
 * the fiels test data can be produced by the Markov chain.  
 *
 */
public class GrammarCheckTestParameterTraverser extends TestParameterTreeTraverser {

	private boolean trainingPhase = true;

	private static final double EPSILON_FACTOR = 100.0;
	
	protected Map<List<String>,List<String>> pathToTrainingData = new HashMap<>();
	private Map<List<String>,MarkovChain> pathToMinedGrammar = new HashMap<>();
	private Map<McState,Set<Symbol>> coveredTransitions = new HashMap<>();
	
	public void clearCoverageData() {
		coveredTransitions = new HashMap<McState, Set<Symbol>>();
	}

	private boolean foundValue = false;;
	public GrammarCheckTestParameterTraverser(int maxDepth, boolean exploreArrays) {
		super(maxDepth, exploreArrays, false);
	}
	private MarkovChain mineGrammar(List<String> trainingDataPara) {

		long combinedLength = 0;
		for(String s : trainingDataPara)
			combinedLength += s.length();
		double epsilon = EPSILON_FACTOR / combinedLength; // data-dependent epsilon for Alergia

		try {
			Alergia alergia = new Alergia(epsilon);
			return alergia.runAlergia(toAlergiaSample(trainingDataPara));
		} catch(Exception e) {
			e.printStackTrace();
			throw e;			
		}
	}
	private List<List<Symbol>> toAlergiaSample(List<String> trainingDataPara) {

		List<List<Symbol>> trainingSample = new ArrayList<List<Symbol>>();
		for(String trainingString : trainingDataPara) {
			List<Symbol> alergiaString = toSymbolString(trainingString);
			trainingSample.add(alergiaString);
		}
		return trainingSample;
	}
	private List<Symbol> toSymbolString(String trainingString) {
		List<Symbol> symbolString = new ArrayList<Symbol>();
		for(int i = 0; i < trainingString.length(); i++) {
			Character rawChar = Character.valueOf(trainingString.charAt(i));
			symbolString.add(new Symbol(rawChar));
		}
		return symbolString;
	}
	public void stopTraining() {
		trainingPhase = false;
		for(Entry<List<String>, List<String>> trainingDataPair : pathToTrainingData.entrySet()) {
			this.getPathToMinedGrammar().put(trainingDataPair.getKey(), 
					mineGrammar(trainingDataPair.getValue()));
		}

	}

	@Override
	protected boolean checkStringForStopping(CharSequence cSeq, Class<?> t, ImmutableList<String> pathIntree) {

		if(cSeq == null) {
			foundValue = false;
			return false;
		}
		List<String> pathAsList = pathIntree.toReversedList();
		if(trainingPhase) {
			List<String> trainingDataForCurrentPath = pathToTrainingData.get(pathAsList);
			if(trainingDataForCurrentPath == null) {
				trainingDataForCurrentPath = new ArrayList<String>();
				pathToTrainingData.put(pathAsList, trainingDataForCurrentPath);
			}
			trainingDataForCurrentPath.add(cSeq.toString());
			foundValue = false;
			return false;
		}
		else {
			if(super.checkStringForStopping(cSeq, t, pathIntree))
				return true;
			else {
				MarkovChain markovChainForPath = getPathToMinedGrammar().get(pathAsList);
				if(markovChainForPath == null) {
					// we know nothing about this path, so we "err on the efficiency site" and avoid running a test
					foundValue = false;
					return false;
				}
				// TODO check if we should add to training data incrementally
				// also after training phase has been completed
//				foundValue = !markovChainForPath.doesLikelyProduce(cSeq.toString());
				Entry<McState, Symbol> nonMatching = markovChainForPath.findNonMatchingSymbol(cSeq.toString());
				if(nonMatching != null && !hasBeenCovered(nonMatching)) {
					foundValue = true;
				} else {
					foundValue = false;
				}
					
				return foundValue;
			}
		}
	}

	private boolean hasBeenCovered(Entry<McState, Symbol> nonMatching) {
		Set<Symbol> coveredSymbolsForState = coveredTransitions.get(nonMatching.getKey());
		if(coveredSymbolsForState == null) {
			coveredSymbolsForState = new HashSet<Symbol>();
			coveredTransitions.put(nonMatching.getKey(), coveredSymbolsForState);
		}
		boolean newValue = coveredSymbolsForState.add(nonMatching.getValue());
		return !newValue;
	}
	public boolean isTrainingPhase() {
		return trainingPhase;
	}

	public void setTrainingPhase(boolean trainingPhase) {
		this.trainingPhase = trainingPhase;
	}
	public boolean hasFoundSuspiciousValue() {
		return foundValue;
	}
	public void resetFoundSuspiciousValue() {
		foundValue = false;
	}
	public Map<List<String>,MarkovChain> getPathToMinedGrammar() {
		return pathToMinedGrammar;
	}
	public void setPathToMinedGrammar(Map<List<String>,MarkovChain> pathToMinedGrammar) {
		this.pathToMinedGrammar = pathToMinedGrammar;
	}

}
