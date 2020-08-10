package fieldtest.triggering.grammar.alergia;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class implements a node in a prefix tree acceptor.
 * 
 */
public class PTANode {
	private Symbol label = null;
	private PTATransition incomingTransition = null;
	private Map<Symbol,PTATransition> successors = new HashMap<>();
	private List<Symbol> stringFromRoot = null;
	private boolean empty = false;

	public Symbol getLabel() {
		return label;
	}

	public void setLabel(Symbol label) {
		this.label = label;
	}

	public PTANode(Symbol label, PTATransition incomingTransition) {
		super();
		this.label = label;
		this.incomingTransition = incomingTransition;
	}
	public Collection<PTATransition> getSuccessors(){
		return successors.values();
	}

	public int transitionFrequency(Symbol symbol){
		PTATransition successorForSymbol = successors.get(symbol);
		if(successorForSymbol == null)
			return 0;
		else
			return successorForSymbol.getFrequency();
	}
	// sum_{\sigma \in \Sigma_out} f(q_this,\sigma}
	public int transitionFrequency(){
		int frequency = 0;
//		return successors.values().stream().mapToInt(t -> t.getFrequency()).sum();
		for(PTATransition succT : successors.values()) {
			frequency += succT.getFrequency();
		}
		return frequency;
	}

	public PTANode addSuccessor(Symbol label) {
		PTATransition succ = getSuccessor(label);
		if(succ != null){
			succ.incrementFequency();
			return succ.getTarget();
		}
		
		PTATransition transToSucc = new PTATransition(this, 1, label);
		successors.put(label,transToSucc);

		return transToSucc.getTarget();
	}

	protected boolean hoeffdingTest(double epsilon, int n_1, int n_2, int f_1, int f_2) {
		if(n_1 == 0 || n_2 == 0)
			return true;
		double lhs = Math.abs((double)f_1/n_1 - (double) f_2/n_2);
		double rhs = (Math.sqrt(1/(double)n_1) + Math.sqrt(1/(double)n_2)) * Math.sqrt(0.5 * Math.log(2/epsilon));
		boolean hoeffdingTest = lhs < rhs;
		return hoeffdingTest;
	}
	public boolean localCompatible(PTANode q_b, 
			double epsilon, Set<Symbol> alphabet) {
		int n_1 = transitionFrequency();
		int n_2 = q_b.transitionFrequency();
		if (n_1 == 0 || n_2 == 0)
			return true;
		for (Symbol o : alphabet) {
			int f_1 = transitionFrequency(o);
			int f_2 = q_b.transitionFrequency(o);
			boolean hoeffdingTest = hoeffdingTest(epsilon, n_1, n_2, f_1, f_2);
			if (!hoeffdingTest)
				return false;
		}
		return true;
		
	}
	public List<Symbol> getStringFromRoot() {
		if(stringFromRoot == null){
			List<Symbol> stringFromRootTemp = new ArrayList<>();
			PTATransition incomingTransitionIterator = incomingTransition;
			Symbol rootSymbol = null;
			while(true){ // note: we do not include the start symbol
				stringFromRootTemp.add(incomingTransitionIterator.getSymbol());
				PTATransition nextTransition = incomingTransitionIterator.getSource().incomingTransition;
				if(nextTransition == null){
					rootSymbol = incomingTransitionIterator.getSource().getLabel();
					break;
				} else {
					incomingTransitionIterator = nextTransition;
				}
			}

			stringFromRootTemp.add(rootSymbol);
			Collections.reverse(stringFromRootTemp);
			stringFromRoot = new ArrayList<>(stringFromRootTemp);
		}
		return stringFromRoot;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

	public PTATransition getSuccessor(Symbol step) {
		return successors.get(step);
	}

	public static PTANode empty(Symbol labelPara) {
		PTANode emptyNode = new PTANode(labelPara, null);
		emptyNode.setEmpty(true);
		return emptyNode;
	}
}
