package fieldtest.usage;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.generator.java.lang.AbstractStringGenerator;

public class SimulateUsageGenerator extends Generator<String> {
    // generate some doubles
    private static final String FLOAT_MAX = "3.4028235e+38";    
    private static final String FLOAT_MAX_PLUS = "3.40282354e+38";
    private static final String FLOAT_MAX_PLUSPLUS = "3.4028236e+38";  
    private static final String DOUBLE_MAX = "1.7976931348623157e+308";
    private static final String DOUBLE_MAX_PLUS = "1.7976931348623159e+308";
    private static final String[] BOUNDARY_VALUES = {FLOAT_MAX,FLOAT_MAX_PLUS, 
        DOUBLE_MAX,DOUBLE_MAX_PLUS};
    private static final double PROB_BOUNDARY = 0.05;

    private static int MAX_LENGTH = 10;

    public SimulateUsageGenerator() {
        super(String.class);
    }

    @Override
    public String generate(SourceOfRandomness random, GenerationStatus status) {
        StringBuilder sb = new StringBuilder();
        if(random.nextBoolean()){
            sb.append('-'); // add a leading minus half the time
        }
        
        if(random.nextDouble() < PROB_BOUNDARY){
            // generate boundary values otherwise
            sb.append(BOUNDARY_VALUES[random.nextInt(BOUNDARY_VALUES.length)]);
        } else {
            if(random.nextBoolean()){
                // generate "normal" double of the form \d+\.\d+
                int lengthFront = 1 + random.nextInt(MAX_LENGTH);  
                int lengthBack = 1 + random.nextInt(MAX_LENGTH);
                for(int i = 0; i < lengthFront; i++)
                    sb.append(random.nextInt(10));
                sb.append('.');
                for(int i = 0; i < lengthBack; i++)
                    sb.append(random.nextInt(10));
            } else {
                // create doubles with exponent (maybe larger than DOUBLE_MAX)
                int lengthDec = 1 + random.nextInt(MAX_LENGTH);  
                int lengthExp = 1 + random.nextInt(3);
                sb.append(random.nextInt(10));
                sb.append('.');
                for(int i = 0; i < lengthDec; i++)
                    sb.append(random.nextInt(10));
                sb.append('e');
                if(random.nextBoolean())
                    sb.append('+');
                else
                    sb.append('-');   // try also negative exponent
                for(int i = 0; i < lengthExp; i++)
                    sb.append(random.nextInt(10));
            }
        }
           
        return sb.toString();
    }
}
