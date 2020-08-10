package fieldtest.triggering.grammar.alergia;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * A class implementing a Markov chain. The Markov chain is a tuple 
 * <S,A,i,\delta>, where S is the set of states represented by 
 * <code>states</code>, A is the alphabet in <code>alphabet</code>, i is the 
 * initial state <code>initialState</code> and \delta is the probabilistic 
 * transition relation that can be accessed from the states in 
 * <code>states</code>.
 * The States are labelled by output symbols.
 * 
 */
public  class MarkovChain {
	
	private Set<McState> states = null;
	private Set<Symbol> alphabet = null;
	private List<List<Symbol>> sampleData = null;
	private IdGen idGen = new IdGen();
	private McState initialState;
	
	private static final double PRODUCTION_PROBABILITY_THRESHOLD = 0.01;
	
	
	public MarkovChain(Set<McState> states, Set<Symbol> alphabet,
			McState initialState,
			Map<McState, ProbabilityDistribution<McState>> transitionProbDist, 
			Map<McState, Symbol> labellingFunction) {
		super();
		this.states = states;
		this.alphabet = alphabet;
		this.initialState = initialState;
	}
	
	public MarkovChain(PrefixTreeAcceptor fpta,  
			List<List<Symbol>> initialSampleData) {
		PTANode root = fpta.getRoot();

		McState initState = new McState(idGen.next(), root.getLabel(), root);
		Map<PTANode,McState> ptaToMcState = new HashMap<>();
		states = new HashSet<>();
		alphabet = new HashSet<>();
		sampleData = initialSampleData;
		
		ptaToMcState.put(root, initState);
		states.add(initState);
		ptaToMC(root, ptaToMcState);
		
		this.initialState = initState;
	}
	/**
	 * This method transforms a prefix tree acceptor into a tree-shaped Markov 
	 * chain with non-normalized transition probabilities.
	 * @param initNode root node of the prefix tree acceptor
	 * @param ptaToMcState mapping between nodes of the tree and states of the 
	 * Markov chain
	 */
	private void ptaToMC(PTANode initNode, Map<PTANode, McState> ptaToMcState) {
		// make breadth-first exploration (does not matter actually, but to have low IDs near the initial state)
		LinkedList<PTANode> schedule = new LinkedList<>();
		schedule.add(initNode);
		while(!schedule.isEmpty()){
			PTANode currentNode = schedule.poll();
			McState currentState = ptaToMcState.get(currentNode);
			// note that start symbol is included
			alphabet.add(currentNode.getLabel()); 
			for(PTATransition succ : currentNode.getSuccessors()){
				alphabet.add(succ.getSymbol());
				PTANode succNode = succ.getTarget();
				
				McState stateForTarget = new McState(idGen.next(), succNode.getLabel(),succNode);
				
				ptaToMcState.put(succNode, stateForTarget);
				states.add(stateForTarget);
				McTransition trans = currentState.addTransition(stateForTarget, 
						succ.getFrequency(), succ.getSymbol());
				stateForTarget.setUniqueIncomingTransition(trans);
				schedule.add(succNode);
			}
		}
	}
	public McState initialState(){
		return initialState;
	}
	public Set<Symbol> getAlphabet() {
		return alphabet;
	}
	public Set<McState> getStates() {
		return states;
	}
	public List<List<Symbol>> getSampleData() {
		return sampleData;
	}
	public void removeState(McState q_b) {
		boolean removed = states.remove(q_b);
		if(!removed)
			System.out.println("weird");
	}
	public IdGen getIdGen() {
		return idGen;
	}	
	// use with care
	public void setIdGen(IdGen idGen) {
		this.idGen = idGen;
	}

	/**
	 * This method checks whether a string can be produced on a symbol-by-symbol
	 * basis.
	 * 
	 * @param symbolString string to be checked
	 * @return true if string is likely to be produced, false otherwise
	 */
	public boolean doesLikelyProduce(List<Symbol> symbolString) {
		McState currentState = initialState;
		for(Symbol s : symbolString) {
			McTransition outgoingTransition = currentState.getTransitions().get(s);
			if(outgoingTransition != null && outgoingTransition.getProbability() 
					>= PRODUCTION_PROBABILITY_THRESHOLD) {
				currentState = outgoingTransition.getTarget();
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * This method also checks whether a string can be produced on a 
	 * symbol-by-symbol basis like the method <code>doesLikelyProduce</code>.
	 * 
	 * @param string string to be checked
	 * @return pair containing the last state reached during and the symbol that 
	 * is unlikely to be produced 
	 */
	public Map.Entry<McState, Symbol> findNonMatchingSymbol(String string) {
		McState currentState = initialState;
		final int len = string.length();
		for(int i = 0; i < len; i ++) {
			Symbol s = new Symbol(string.charAt(i));
			McTransition outgoingTransition = currentState.getTransitions().get(s);
			if(outgoingTransition != null && outgoingTransition.getProbability() 
					>= PRODUCTION_PROBABILITY_THRESHOLD) {
				currentState = outgoingTransition.getTarget();
			} else {
				return new AbstractMap.SimpleEntry<>(currentState, s);
			}
		}
		return null;
	}
	/**
	 * This method checks whether a string can be produced on a symbol-by-symbol
	 * basis.
	 * 
	 * @param string string to be checked
	 * @return true if string is likely to be produced, false otherwise
	 */
	public boolean doesLikelyProduce(String string) {
		McState currentState = initialState;
		final int len = string.length();
		for(int i = 0; i < len; i ++) {
			Symbol s = new Symbol(string.charAt(i));
			McTransition outgoingTransition = currentState.getTransitions().get(s);
			if(outgoingTransition != null && outgoingTransition.getProbability() 
					>= PRODUCTION_PROBABILITY_THRESHOLD) {
				currentState = outgoingTransition.getTarget();
			} else {
				return false;
			}
		}
		return true;
	}
}
