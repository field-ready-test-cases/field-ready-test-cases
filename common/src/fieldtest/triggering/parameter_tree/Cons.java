package fieldtest.triggering.parameter_tree;

import java.util.ArrayList;
import java.util.List;

/**
 * A constructor of an immutable list consisting of head element and the tail of 
 * the list. 
 *
 * @param <T> the data type of the elements in the list
 */
public class Cons<T> implements ImmutableList<T> {
	private ImmutableList<T> tail;
	private T head;
	public Cons(T head, ImmutableList<T> tail) {
		this.head = head;
		this.tail = tail;
	}
	@Override
	public List<T> toReversedList() {
		List<T> tailList = tail.toReversedList();
		tailList.add(head);
		return tailList;
	}
	@Override
	public List<T> toList() {
		List<T> prefix = new ArrayList<>();
		prefix.add(head);
		return tail.toList(prefix);
	}
	@Override
	public List<T> toList(List<T> prefix) {
		prefix.add(head);
		return tail.toList(prefix);
	}
}
