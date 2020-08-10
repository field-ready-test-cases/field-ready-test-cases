package fieldtest.usage;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.generator.java.lang.AbstractStringGenerator;

public class SimulateUsageGenerator extends Generator<String> {
    // generate some decimal and integral numbers ending with (double) exponent markers, e, E, eE, or Ee
    public static final int MAX_LENGTH = 10;
    public static final double eE_PROB = 0.025;

    public SimulateUsageGenerator() {
        super(String.class);
    }

    @Override
    public String generate(SourceOfRandomness random, GenerationStatus status) {
        StringBuilder sb = new StringBuilder();
        int lengthPrefix= 1 + random.nextInt(MAX_LENGTH); 
       
        for (int i = 0; i < lengthPrefix; i++) {
            sb.append(random.nextChar('0','9'));
        }
        if(random.nextBoolean()){
            int lengthSuffix= 1 + random.nextInt(MAX_LENGTH); 
            sb.append('.');
            for (int i = 0; i < lengthSuffix; i++) {
                sb.append(random.nextChar('0','9'));
            }
        }
        if(random.nextDouble() < eE_PROB){
            switch(random.nextInt(4)){
                case 0:
                    sb.append("e");
                    break;
                case 1:
                    sb.append("E");
                    break;
                case 2:
                    sb.append("eE");
                    break;
                case 3:
                default:
                    sb.append("Ee");
                    break;
            }
        }
                  
        return sb.toString();
    }
}
