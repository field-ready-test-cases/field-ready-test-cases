package fieldtest.triggering.simple;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fieldtest.triggering.FieldTestTrigger;

/**
 * A field test trigger that triggers field-test execution with a given 
 * probability.
 *
 */
public class RandomTestTrigger implements FieldTestTrigger {

	private double triggerProbability = 0;
	private long seed = System.currentTimeMillis();
	private Random random = null;
	
	public RandomTestTrigger(double triggerProbability) {
		super();
		this.triggerProbability = triggerProbability;
		this.random = new Random(seed);
	}

	public RandomTestTrigger(double triggerProbability, long seed) {
		super();
		this.triggerProbability = triggerProbability;
		this.seed = seed;
		this.random = new Random(seed);
	}

	@Override
	public boolean checkTrigger() {
		return random.nextDouble() < triggerProbability;
	}

	@Override
	public List<String> additionalTriggerInformation() {
		return new ArrayList<String>();
	}

	@Override
	public String lastAdditionalTriggerInformation() {
		return null;
	}

}
