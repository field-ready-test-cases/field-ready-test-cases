package fieldtest.triggering.grammar.alergia;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * A state in a Markov chain. The state is labelled with a symbol and "knows"
 * its outgoing transitions.  
 *
 */
public class McState implements Comparable<McState>{
	
	protected String id = "";
	protected Symbol label = null;
	protected Map<Symbol,McTransition> transitions = null;
	private PTANode ptaNode;
	private Optional<McTransition> uniqueIncomingTransition = Optional.empty();
	private Integer intId;
	
	public Map<Symbol,McTransition> getTransitions() {
		return transitions;
	}

	// package protected
	McState(String id, Symbol label, PTANode ptaNode) {
		super();
		this.id = id;
		this.intId = new Integer(id.replace("s", ""));
		this.label = label;
		this.transitions = new HashMap<>();
		this.ptaNode = ptaNode;
	}
	public String getId() {
		return id;
	}
	// use with care
	public void setId(String id) {
		this.id = id;
		this.intId = new Integer(id.replace("s", ""));
	}	
	
	public McTransition addTransition(McState target, double probability, Symbol step){
		McTransition trans = new McTransition(this, target, probability, step);
		transitions.put(step,trans);
		return trans;
	}
	// id suffices for checking equality
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		McState other = (McState) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	// consistent with equals -> only checks id
	// maybe change, as s19 < s2 using this implementation (but lexicographical order is probably not that 
	// significant in Alergia)
	@Override
	public int compareTo(McState o) {
		return intId.compareTo(o.intId);
	}

	public Symbol getLabel() {
		return label;
	}

	public boolean isEmpty() {
		return "empty".equals(id);
	}

//	public abstract boolean localCompatible(McState<S> q_b, SampleData<S> sampleData, 
//			double epsilon);
	
	protected boolean hoeffdingTest(double epsilon, int n_1, int n_2, int f_1, int f_2) {
		if(n_1 == 0 || n_2 == 0)
			return true;
		boolean hoeffdingTest = Math.abs((double)f_1/n_1 - (double) f_2/n_2) <
				(Math.sqrt(1/(double)n_1) + Math.sqrt(1/(double)n_2)) * Math.sqrt(0.5 * Math.log(2/epsilon));
		return hoeffdingTest;
	}

	public Optional<McTransition> getUniqueIncomingTransition() {
		return uniqueIncomingTransition;
	}

	public void setUniqueIncomingTransition(McTransition uniqueIncomingTransition) {
		this.uniqueIncomingTransition = Optional.of(uniqueIncomingTransition);
	}
	public  void invalidateUniqueIncomingTransition(){
		this.uniqueIncomingTransition = Optional.empty();
	}

	public PTANode getPtaNode() {
		return ptaNode;
	}

	public void normalize() {
		double total = 0;
		for(McTransition t : getTransitions().values()) {
			total += t.getProbability();
		}
		for(McTransition t : getTransitions().values()) {
			t.setNormalized(true);
			t.setProbability(t.getProbability()/total);
		}
	}
}
