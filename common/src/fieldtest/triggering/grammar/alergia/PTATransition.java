package fieldtest.triggering.grammar.alergia;

/**
 * This class implements an edge between two nodes in a prefix tree acceptor.
 * 
 */
public class PTATransition {

	private PTANode source = null;
	private PTANode target = null;
	private int frequency = 0;
	private Symbol symbol;
	public PTATransition(PTANode source, int frequency, Symbol symbol) {
		super();
		this.source = source;
		this.target = new PTANode(symbol, this);
		this.frequency = frequency;
		this.symbol = symbol;
	}
	public PTANode getSource() {
		return source;
	}
	public void setSource(PTANode source) {
		this.source = source;
	}
	public PTANode getTarget() {
		return target;
	}
	public void setTarget(PTANode target) {
		this.target = target;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	public void incrementFequency() {
		frequency ++;
	}
	
	public Symbol getSymbol() {
		return symbol;
	}
}
