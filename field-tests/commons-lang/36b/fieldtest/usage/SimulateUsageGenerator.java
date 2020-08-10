package fieldtest.usage;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.generator.java.lang.AbstractStringGenerator;

public class SimulateUsageGenerator extends Generator<String> {
    // generate some integral numbers ending with '.' ,i.e., actually decimals
    public static final int MAX_LENGTH = 10;
    public static final double DOT_PROB = 0.025;
    public static final double SIGN_PROB = 0.25;

    public SimulateUsageGenerator() {
        super(String.class);
    }

    @Override
    public String generate(SourceOfRandomness random, GenerationStatus status) {
        StringBuilder sb = new StringBuilder();
        int lengthPrefix= 1 + random.nextInt(MAX_LENGTH); 
        
        if(random.nextDouble() < SIGN_PROB){
            sb.append(random.nextBoolean() ? '+' : '-');
        }
        // don't start with 0 to avoid octal numbers
        sb.append(random.nextChar('1','9'));
        for (int i = 1; i < lengthPrefix; i++) {
            sb.append(random.nextChar('0','9'));
        }
       
        if(random.nextDouble() < DOT_PROB)
            sb.append(".");
                  
        return sb.toString();
    }
}
