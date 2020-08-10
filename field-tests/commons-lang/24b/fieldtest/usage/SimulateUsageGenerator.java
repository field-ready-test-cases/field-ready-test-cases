package fieldtest.usage;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.generator.java.lang.AbstractStringGenerator;

public class SimulateUsageGenerator extends Generator<String> {
    // generate some potentially long decimal numbers with an L 
    // and generate some potentially long integer ending with an D or F
    public static final int MAX_LENGTH = 14;
    private static final double L_DF_PROB = 0.025;

    public SimulateUsageGenerator() {
        super(String.class);
    }

    @Override
    public String generate(SourceOfRandomness random, GenerationStatus status) {
        StringBuilder sb = new StringBuilder();
        int lengthPrefix= 1 + random.nextInt(MAX_LENGTH);
        int lengthSuffix= 1 + random.nextInt(MAX_LENGTH);
        boolean decimal = random.nextBoolean();
        
        for (int i = 0; i < lengthPrefix; i++) {
            sb.append(random.nextChar('0','9'));
        }
        
        if(decimal){
            sb.append('.');
            for (int i = 0; i < lengthSuffix; i++) {
                sb.append(random.nextChar('0','9'));
            }
            if(random.nextDouble() < L_DF_PROB)
                sb.append(random.nextBoolean() ? 'L' : 'l');
        } else {
            if(random.nextDouble() < L_DF_PROB){
                if(random.nextBoolean())
                    sb.append(random.nextBoolean() ? 'F' : 'f');
                else
                    sb.append(random.nextBoolean() ? 'D' : 'd');
            }
        }
        return sb.toString();
    }
}
