package fieldtest.usage;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.generator.java.lang.AbstractStringGenerator;

public class SimulateUsageGenerator extends Generator<String> {
    // generate some integers potentially ending with an l and potentially 
    // starting with a zero or a minus sign, or both
    private static final int MAX_LENGTH = 16;

    // add some random garbage with probability 0.05
    private static final double GARBAGE_PROBABILITY = 0.05; 
    private static final double PROB_l = 0.05; 

    public SimulateUsageGenerator() {
        super(String.class);
    }

    @Override
    public String generate(SourceOfRandomness random, GenerationStatus status) {
        StringBuilder sb = new StringBuilder();
        switch(random.nextInt(8)){
            case 0: 
                sb.append('0');
                break;
            case 1: 
                sb.append('-');
                break;
            case 2: 
                sb.append("-0");
                break;
            default: // start with nothing special in most of the cases
                break;
        }
        int length = random.nextInt(1, MAX_LENGTH);
        for(int i = 0; i < length; i++){
            sb.append(random.nextChar('0','9'));
        }
        
        // add some random garbage
        if(random.nextDouble() < GARBAGE_PROBABILITY){
            do{
                sb.insert(random.nextInt(sb.length()+1), random.nextChar('!', 'z'));
            }while(random.nextDouble() < GARBAGE_PROBABILITY);
        }
        if(random.nextDouble() < PROB_l){
            sb.append('l');
        }
        return sb.toString();
    }
}
