package fieldtest.triggering.grammar.alergia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import fieldtest.logging.SingletonFieldTestLogger;
/**
 * An implementation of the automata-learning algorithm Alergia. Alergia
 * was first presented in 
 * "Learning Stochastic Regular Grammars by Means of a State Merging Method"
 * by Carrasco and Oncina (doi: 10.1007/3-540-58473-0_144). This implementation 
 * is mostly based on the presentation in 
 * "Learning deterministic probabilistic automata from a model checking 
 * perspective" by Mao et al. (doi:10.1007/s10994-016-5565-9).
 */
public class Alergia{

	private double epsilon;
	private boolean debug = false;
	
	public Alergia(double epsilon){
		this.epsilon = epsilon;
	}

	public PrefixTreeAcceptor createPrefixTreeAcceptor(List<List<Symbol>> sample){
		PrefixTreeAcceptor pta = new PrefixTreeAcceptor();
		for(List<Symbol> s : sample)
			pta.add(s);
		return pta;
	}
	public MarkovChain runAlergia(List<List<Symbol>> sample){

		PrefixTreeAcceptor fpta = createPrefixTreeAcceptor(sample);

		MarkovChain mc = new MarkovChain(fpta, sample);

		McState initState = mc.initialState(); // we know here that we have a deterministic MC

		ArrayList<McState> red = new ArrayList<>(); // sorted list

		red.add(initState);
		List<McState> blue = succs(red);
		System.out.println("Initial number of states: " + mc.getStates().size());
		while(!blue.isEmpty()){
			// straight-forward implementation following paper by Mao et al.
			McState q_b = blue.get(0);
			boolean merged = false;
			Iterator<McState> redIter = red.iterator();
			McState compatibleState = null;

			while (redIter.hasNext()) {
				McState q_r = redIter.next();
				if (compatible(fpta, q_r.getPtaNode(), q_b.getPtaNode(), mc, epsilon,0,mc.getAlphabet())) {
					compatibleState = q_r; 
					break; // comment out to use ad-hoc-calculated certainty value
				}
			}

			if(compatibleState != null){
				merge(mc, compatibleState,q_b);
				merged = true;
				if(debug)
					System.out.println("Merged " + q_b.getId() + " into " +compatibleState.getId());
			}

			if(!merged){
				if(debug)
					System.out.println("Created red state " + q_b.getId());
				insertIntoSortedList(red,q_b);
			}
			// it's hard to do it more efficient than that because
			// you might think that you only need to consider successors of q_r (the target of a possible merge) or q_b
			// but this is not true because merging changes the structure in a way, such that nodes other than q_r/q_b
			// can get new successors (because the subtree rooted q_b is RECURSIVELY folded into q_r)
			blue = succs(red);
		
			if(debug) {
				System.out.println("Blue now: ");
				for(McState state : blue) {
					System.out.print(state.getId() + ", ");
				}
				System.out.println();
			}
		}

		System.out.println("System has " + mc.getStates().size() + " states");
		return normalize(mc);
	}

	private MarkovChain normalize(MarkovChain mc) {
		for(McState state : mc.getStates())
			state.normalize();
		return mc;
	}
	
	protected void insertIntoSortedList(ArrayList<McState> sortedList, McState newState) {
		ListIterator<McState> listIter = sortedList.listIterator();
		while(listIter.hasNext()){
			McState currentElem = listIter.next();
			if(newState.compareTo(currentElem) < 0){
				listIter.previous();
				listIter.add(newState);
				return;
			}
		}
		sortedList.add(newState);
	}
	private void merge(MarkovChain mc, McState q_r, McState q_b) {
		McTransition incomingTrans = q_b.getUniqueIncomingTransition().get();
		q_b.invalidateUniqueIncomingTransition();
		incomingTrans.setTarget(q_r);
		fold(q_b,q_r, mc);
		mc.removeState(q_b);
	}
	private void fold(McState q_b, McState q_r, MarkovChain mc) {
		for(McTransition outgoingT : q_b.getTransitions().values()){
			McTransition matchtingTrans = null;
			for(McTransition t : q_r.getTransitions().values()) {
				if(t.getStepSymbol().mappedEquals(outgoingT.getStepSymbol()))
					matchtingTrans = t;
			}

			if(matchtingTrans != null) {// .isPresent()){
//				McTransition actualMatchingTrans = matchtingTrans.get();
				matchtingTrans.setProbability(
						matchtingTrans.getProbability() + outgoingT.getProbability());
				mc.removeState(outgoingT.getTarget());
				fold(outgoingT.getTarget(),matchtingTrans.getTarget(),mc);
			} else {
				// simply transit to successor of blue state
				McTransition newTrans = q_r.addTransition(outgoingT.getTarget(), outgoingT.getProbability(), 
						outgoingT.getStepSymbol());
				outgoingT.getTarget().setUniqueIncomingTransition(newTrans);
				
			}
		}
	}
	private boolean compatible(PrefixTreeAcceptor fpta, PTANode q_b, PTANode q_r, MarkovChain mc, 
		 double epsilon, int depth, Set<Symbol> alphabet) {

		if(q_b.isEmpty() || q_r.isEmpty())
			return true;
		if(!q_b.getLabel().mappedEquals(q_r.getLabel()))
			return false;

		if(!localCompatible(q_b,q_r,epsilon, alphabet))
			return false;
		for(Symbol step: mc.getAlphabet()){
			PTANode q_bp = succ(q_b,step);
			PTANode q_rp = succ(q_r,step); 
			if(!compatible(fpta, q_bp, q_rp, mc,epsilon, depth + 1, alphabet))
				return false;
		}

		return true;
	}
	
	private PTANode succ(PTANode q_b, Symbol label) {
		PTATransition succ = q_b.getSuccessor(label);

		return succ == null ? PTANode.empty(label): succ.getTarget();
	}
	private boolean localCompatible(PTANode q_b, PTANode q_r,  double epsilon, Set<Symbol> alphabet) {
		return q_b.localCompatible(q_r, epsilon, alphabet);
	}
	// use list to sort states by id (lexicographical minimal elem should be chosen in each iteration 
	private List<McState> succs(List<McState> red) {
		Set<McState> redSet = new HashSet<>(red); 
		Set<McState> resultSet = new HashSet<>();

		for(McState r : red){
			for(McTransition t : r.getTransitions().values()){ 
				if(!redSet.contains(t.getTarget()))
					resultSet.add(t.getTarget());
			}
		}
		List<McState> result = new ArrayList<>(resultSet);
		Collections.sort(result);
		return result;
	}

	public boolean isDebug() {
		return debug;
	}
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}
}
