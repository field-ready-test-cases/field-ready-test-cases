package fieldtest.usage;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.generator.java.lang.AbstractStringGenerator;

public class SimulateUsageGenerator extends Generator<JoinArgs> {
	private static final int MAX_ARRAY_SIZE = 4;
    private static final double NULL_PROBABILITY = 0.05;
	private static final double WRAP_PROBABILITY = 0.33; // "simulate" complex objects

    public SimulateUsageGenerator() {
        super(JoinArgs.class);
    }

    private Object generateElem(SourceOfRandomness random){
        // switch between four interesting options
    	if(random.nextDouble() < WRAP_PROBABILITY) {
    		// wrap in OnePlaceBuffer (can be wrapped multiple times
    		return new OnePlaceBuffer(generateElem(random));
    	}
    	if(random.nextDouble() < NULL_PROBABILITY) {
            return null;
    	} else {
            switch(random.nextInt(3)){
                case 0:
                    return "";
                case 1:
                    //simply return any string
                    return "foo";
                case 2:
                default: // simply return any other object with non-empty toString()
                    return 3;
            }
        }
    }
    @Override
    public JoinArgs generate(SourceOfRandomness random, GenerationStatus status) {
        JoinArgs args = new JoinArgs();
        if(random.nextDouble() < NULL_PROBABILITY)
            return null; 
        // argument is exclusive
        int arraySize = random.nextInt(MAX_ARRAY_SIZE + 1); 
        Object elems[] = new Object[arraySize];

        for(int i = 0; i < elems.length; i++){
    		elems[i] = generateElem(random);
        }
        args.elems = elems;
        args.separator = '/'; // use some non-regex relevant character

        if(random.nextBoolean()){ // create any indexes half the time
            args.startIndex = random.nextInt(0,MAX_ARRAY_SIZE);
            args.endIndex = random.nextInt(0,MAX_ARRAY_SIZE);
        } else { // create indexes that make sense
            args.startIndex = arraySize == 0 ? 0 : random.nextInt(0,arraySize - 1);
            args.endIndex = random.nextInt(args.startIndex,arraySize);
        }
               
        return args;
    }
}
