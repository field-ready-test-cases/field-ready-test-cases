package fieldtest.usage;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.generator.java.lang.AbstractStringGenerator;

public class SimulateUsageGenerator extends Generator<String> {
    // generate some strings containing (un)finished numeric entities
    public static final int MAX_LENGTH_STRING = 10;
    public static final int MAX_LENGTH_ENTITY = 4;
    public static final int MIN_LENGTH_ENTITY = 2;

    private static final String UPPERCASE_CHARS = "ABCDEF";
    private static final String NUMBERS = "0123456789";
    private static final String UPPERCASE_DIGITS = UPPERCASE_CHARS
          + NUMBERS;    

    private static final double STOP_PROBABILITY_ENTITY_GEN = 0.1;

    public SimulateUsageGenerator() {
        super(String.class);
    }

    private String generateNumericEntity(SourceOfRandomness random){
        StringBuilder entity = new StringBuilder();
        entity.append('&');
        // return with probability STOP_PROBABILITY_ENTITY_GEN
        if(random.nextDouble() < STOP_PROBABILITY_ENTITY_GEN) 
            return entity.toString();
        entity.append('#');
        if(random.nextDouble() < STOP_PROBABILITY_ENTITY_GEN)
            return entity.toString();
        if(random.nextBoolean()){ // x may or may not be present
            entity.append(random.nextBoolean() ? 'x' : 'X');
        }
        int entityLen = random.nextInt(2,4);
        for (int i = 0; i < entityLen; i++){
            entity.append(UPPERCASE_DIGITS.charAt(random.nextInt(UPPERCASE_DIGITS.length())));
        }
        if(random.nextDouble() < STOP_PROBABILITY_ENTITY_GEN)
            return entity.toString();
        entity.append(';');
        return entity.toString();
    }

    @Override
    public String generate(SourceOfRandomness random, GenerationStatus status) {
        StringBuilder sb = new StringBuilder();
        int length = 1 + random.nextInt(MAX_LENGTH_STRING); // don't allow length of zero
        
        for (int i = 0; i < length; i++){
            sb.append(random.nextChar('A', 'z'));
        }
        String entity = generateNumericEntity(random);
        // insert at random position with some white space
        sb.insert(random.nextInt(sb.length()+1), " " + entity + " "); 
           
        return sb.toString();
    }
}
