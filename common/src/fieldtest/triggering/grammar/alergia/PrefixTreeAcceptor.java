package fieldtest.triggering.grammar.alergia;

import java.util.List;

/**
 * A class implementing a prefix tree acceptor (PTA). A PTA represents a 
 * multiset of strings S, where frequencies in the PTA denote how often some 
 * prefix is present in S.  
 *
 */
public class PrefixTreeAcceptor {

	// all strings start with the same symbol
	private Symbol rootSymbol = Symbol.ROOT;
	private PTANode root = null;
	
	public PrefixTreeAcceptor() {
		init();
	}
	
	public PTANode getRoot() {
		return root;
	}
	public void setRoot(PTANode root) {
		this.root = root;
	}
	private void init(){
		root = new PTANode(rootSymbol, null);
	}
	public void add(List<Symbol> string){
		add(root,string);
	}
	private void add(PTANode current, List<Symbol> string) {
		if(string.size() == 0){
			return;
		}
		else {
			PTANode succNode = current.addSuccessor(string.get(0));
			add(succNode,string.subList(1, string.size()));
		}
	}
}
