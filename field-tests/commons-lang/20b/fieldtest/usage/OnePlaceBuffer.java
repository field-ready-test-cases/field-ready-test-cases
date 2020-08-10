package fieldtest.usage;

public class OnePlaceBuffer{
	private Object bufferedElem = null;
	
	public OnePlaceBuffer(Object bufferedElem) {
		super();
		this.bufferedElem = bufferedElem;
	}

	@Override
	public String toString() {
		if(bufferedElem == null)
			return null;
		else 
			return bufferedElem.toString();
	}
    
}