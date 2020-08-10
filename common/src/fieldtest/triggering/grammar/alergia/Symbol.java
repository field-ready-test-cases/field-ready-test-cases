package fieldtest.triggering.grammar.alergia;
/**
 * A generic "symbol" utility class. This class is used to label states in 
 * Markov chains. 
 * 
 */
public class Symbol {
	
	public static final Symbol ROOT = new Symbol(null);
	private Character rawSymbol;
	private Character mappedSymbol;
	public static boolean DO_NOT_MAP = false;
	public Symbol (Character rawSymbolPara){
		this.rawSymbol = rawSymbolPara;
		this.mappedSymbol = map(rawSymbolPara);
	}

	public String stringRepresentation() {
		return "" + mappedSymbol;
	}

	// implement character classes here
	private Character map(Character rawSymbolPara) {
		if(DO_NOT_MAP)
			return rawSymbolPara;
		if(rawSymbolPara == null)
			return null;		
		if(rawSymbolPara >= 'a' && rawSymbolPara <= 'z') {
			return 'a'; // map all lowercase letters to lowercase a
		} else if(rawSymbolPara >= 'A' && rawSymbolPara <= 'Z'){
			return 'A';
		} else if(rawSymbolPara >= '1' && rawSymbolPara <= '9') {
			// "digit character class" does not include zero, because zero often
			// serves a special purpose, e.g., a leading zero marks 
			// octal numbers
			return '1'; 
		}
		else {
			return rawSymbolPara;
		}
	}

	public boolean mappedEquals(Symbol other) {
		if(this.mappedSymbol == null)
			return other.mappedSymbol == null;
		else
			return this.mappedSymbol.equals(other.mappedSymbol);
	}

	@Override
	public String toString() {
		return "Symbol [rawSymbol=" + rawSymbol + ", mappedSymbol=" + mappedSymbol + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		if(DO_NOT_MAP) {
			int result = 1;
			result = prime * result + ((mappedSymbol == null) ? 0 : mappedSymbol.hashCode());
			result = prime * result + ((rawSymbol == null) ? 0 : rawSymbol.hashCode());
			return result;
		} else {
			if(mappedSymbol == null)
				return prime;
			else
				return mappedSymbol.hashCode();
		}
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Symbol other = (Symbol) obj;
		if (mappedSymbol == null) {
			if (other.mappedSymbol != null)
				return false;
		} else if (!mappedSymbol.equals(other.mappedSymbol))
			return false;
		if(DO_NOT_MAP) { //only check raw symbol if we do not map to character classes
			if ( rawSymbol == null) {
				if (other.rawSymbol != null)
					return false;
			} else if (!rawSymbol.equals(other.rawSymbol))
				return false;
		}
		return true;
	}
}
