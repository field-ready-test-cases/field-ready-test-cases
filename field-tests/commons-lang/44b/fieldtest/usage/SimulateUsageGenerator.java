package fieldtest.usage;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.generator.java.lang.AbstractStringGenerator;

public class SimulateUsageGenerator extends Generator<String> {
    // generate some bad strings 
    private static final String[] badInputs = 
        new String[] { "l", "L", "f", "F", "junk", "bobL"};
    private static final double BAD_PROBABILITY = 0.025;

    public SimulateUsageGenerator() {
        super(String.class);
    }

    @Override
    public String generate(SourceOfRandomness random, GenerationStatus status) {
        if(random.nextDouble() < BAD_PROBABILITY)
            return badInputs[random.nextInt(badInputs.length)];
        else {
            switch(random.nextInt(4)){
                case 0:
                    return Integer.toString(random.nextInt());
                case 1:
                    return Long.toString((long)random.nextInt());
                case 2:
                    return Float.toString((float) random.nextDouble() * random.nextInt());
                case 3:
                default:
                    return Double.toString(random.nextDouble() * random.nextInt());
            }
        }
    }
}
