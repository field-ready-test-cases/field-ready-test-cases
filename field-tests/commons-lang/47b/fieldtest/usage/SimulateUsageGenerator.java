package fieldtest.usage;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.generator.java.lang.AbstractStringGenerator;

public class SimulateUsageGenerator extends Generator<PadArguments> {


    public SimulateUsageGenerator() {
        super(PadArguments.class);
    }

    private char randomChar(SourceOfRandomness random){
        return random.nextChar('!','}');
    }
    private String generateRandomString(SourceOfRandomness random, int maxLength){
        int length = random.nextInt(0, maxLength);
        StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < length; i++){
            sb.append(random.nextChar('!','}'));
        }
        return sb.toString();
    }
    private static final int MAX_LENGTH = 10;
    private static final int MAX_LENGTH_APPEND_STRING = 256;
    private static final double NULL_PROBALITY =0.05;
    
    @Override
    public PadArguments generate(SourceOfRandomness random, GenerationStatus status) {

        
        PadArguments args = new PadArguments();
        args.s = generateRandomString(random, MAX_LENGTH);
        if(random.nextDouble() < NULL_PROBALITY)
            args.obj = null;
        else
            args.obj = generateRandomString(random,MAX_LENGTH_APPEND_STRING);
        args.width = random.nextInt(0, MAX_LENGTH_APPEND_STRING);
        if(random.nextBoolean() && args.s.length() > 0)
            args.padChar = args.s.charAt(args.s.length() - 1);
        else
            args.padChar = randomChar(random);
        return args;
    }
}
