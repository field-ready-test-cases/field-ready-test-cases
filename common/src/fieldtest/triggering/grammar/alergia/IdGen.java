package fieldtest.triggering.grammar.alergia;

import java.util.Iterator;
/**
 * A utility class for generating identifiers to label states of a Markov 
 * chain. It can be used like an endless iterator. 
 * 
 */
public class IdGen implements Iterator<String> {
	private int intId = 0;

	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public String next() {
		return "s" + (intId++);
	}

}
