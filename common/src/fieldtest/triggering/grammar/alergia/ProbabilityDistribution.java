package fieldtest.triggering.grammar.alergia;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A utility of class implementing a probability mass function to encoding 
 * information about a discrete probability distribution. That is, this class 
 * implements a mapping from a discrete set of values to [0,1] such that the 
 * sum over the mapping's image is equal to one. 
 *
 * @param <T> the data type over which the distribution is defined
 */
public class ProbabilityDistribution<T> {
	private Map<T,Double> actualDistribution = null;

	public ProbabilityDistribution(Map<T, Double> actualDistribution) {
		super();
		this.actualDistribution = actualDistribution;
	}

	public static <T> ProbabilityDistribution<T> 
					  singletonDistribution(T elemWithProbOne){
		Map<T,Double> actualDistribution = new HashMap<>();
		// all others are implicitly zero
		actualDistribution.put(elemWithProbOne, 1.0);
		return new ProbabilityDistribution<>(actualDistribution);
	}
	public Optional<T> singleElem(){
		if(actualDistribution.size() == 1){
			return Optional.of(actualDistribution.keySet().iterator().next());
		}
		else {
			return Optional.empty();
		}
	}
}
