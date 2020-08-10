package fieldtest.triggering.parameter_tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
/**
 * A class representing an empty of arbitrary type.
 * 
 * @param <T> the data type of the list
 */
public class Nil<T> implements ImmutableList<T> {

	@Override
	public List<T> toReversedList() {
		return new ArrayList<T>();
	}

	@Override
	public List<T> toList(List<T> prefix) {
		return prefix;
	}

	@Override
	public List<T> toList() {
		return new ArrayList<T>();
	}

}
