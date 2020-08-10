package fieldtest.triggering.grammar.alergia;

/**
 * A transition in a Markov chain. Formally, this is a tuple <s,a,p,t>, where 
 * s is the source state, a is the symbol labelling the transition (also 
 * labelling the target state), p is the transition probability, and t is the 
 * target state. 
 */
public class McTransition {

	private McState source = null;
	private McState target = null;
	private double probability = 0.0;
	private boolean normalized = false; // if normalized == false, probability = frequency
	private Symbol stepSymbol = null;
	
	public McTransition(McState source, McState target, double probability, Symbol stepSymbol) {
		super();
		this.source = source;
		this.target = target;
		this.probability = probability;
		this.stepSymbol  = stepSymbol;
	}
	
	public McState getSource() {
		return source;
	}
	public void setSource(McState source) {
		this.source = source;
	}
	public McState getTarget() {
		return target;
	}
	public void setTarget(McState target) {
		this.target = target;
	}
	public double getProbability() {
		return probability;
	}
	public void setProbability(double probability) {
		this.probability = probability;
	}

	public boolean isNormalized() {
		return normalized;
	}

	public void setNormalized(boolean normalized) {
		this.normalized = normalized;
	}

	public Symbol getStepSymbol() {
		return stepSymbol;
	}
}
