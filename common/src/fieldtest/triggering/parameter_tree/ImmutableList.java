package fieldtest.triggering.parameter_tree;

import java.util.List;
/**
 * 
 *
 * A simple immutable list mainly intended to represent paths in parameter 
 * trees, so instantiations will mostly be ImmutableList<Class<?>> or maybe
 * ImmutableList<String> where the strings are class names.
 * 
 * We do not use existing immutable lists to keep the dependencies low in order 
 * to avoid name clashes, which may occur if we have dependency x, but want to 
 * test a lower version of dependency x.
 *
 * @param <T>
 */
public interface ImmutableList<T> {
	List<T> toReversedList();
	List<T> toList(List<T> prefix);
	List<T> toList();
}
