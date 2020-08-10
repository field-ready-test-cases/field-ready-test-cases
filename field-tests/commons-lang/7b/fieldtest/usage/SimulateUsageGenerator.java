package fieldtest.usage;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.generator.java.lang.AbstractStringGenerator;

public class SimulateUsageGenerator extends Generator<String> {
    // generate some big decimals
    private static int MAX_LENGTH = 10;
    private static int MAX_LENGTH_EXP = 3; 
    private static final double PROB_DOUBLE_MINUS = 0.025; 

    public SimulateUsageGenerator() {
        super(String.class);
    }

    @Override
    public String generate(SourceOfRandomness random, GenerationStatus status) {
        StringBuilder sb = new StringBuilder();
        // add leading minus signs
        if(random.nextBoolean()){
            sb.append('-');
            if(random.nextDouble() < PROB_DOUBLE_MINUS){
                sb.append('-');
            }
        }
        boolean createBigDecimal = random.nextBoolean();
        int lengthDec = 1 + random.nextInt(MAX_LENGTH);  
        int lengthExp = createBigDecimal ? 3 : random.nextInt(1,3); // exponent length should be three to ensure that BigDecimal is required
        sb.append(random.nextInt(1,9));
        sb.append('.');
        for(int i = 0; i < lengthDec; i++)
            sb.append(random.nextInt(10));
        sb.append('e');
        // first digit of exponent larger than 3 to ensure that we generate BigDecimal
        sb.append(createBigDecimal? random.nextInt(4,9):random.nextInt(1,9)); 
        for(int i = 1; i < lengthExp; i++){
            sb.append(random.nextInt(10));
        }       
           
        return sb.toString();
    }
}
