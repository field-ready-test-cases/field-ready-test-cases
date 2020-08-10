package fieldtest.usage;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.generator.java.lang.AbstractStringGenerator;
import java.util.List;
import java.util.Arrays;

public class SimulateUsageGenerator extends Generator<String> {
    // generate some Boolean Strings
    public static final int MAX_LENGTH_OTHER = 5;
    
    public static final List<String> TRUE_STRINGS = Arrays.asList(
        new String[]{"on","yes", "true"});
    public static final List<String> FALSE_STRINGS = Arrays.asList(
        new String[]{"false","off"});
    public static final double NULL_PROB = 0.1;
    public static final double APPEND_RANDOM_PROB = 0.15;
    public static final double REMOVE_SUFFIX_PROB = 0.15;


    public SimulateUsageGenerator() {
        super(String.class);
    }

    private String mutateCase(String inString, SourceOfRandomness random){
        if(random.nextBoolean()) // return unchanged
            return inString;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < inString.length(); i++){
            char currChar = inString.charAt(i);            
            if(random.nextBoolean()){
                if(Character.isUpperCase(currChar))
                    currChar = Character.toLowerCase(currChar);
                else
                    currChar = Character.toUpperCase(currChar);
            }
            sb.append(currChar);
        }
        return sb.toString();
    }
    private String removeSuffix(String inString, SourceOfRandomness random){
        int suffixLen = 1 + random.nextInt(inString.length());
        return inString.substring(0, inString.length() - suffixLen);
    }   
    private String generateRandomString(SourceOfRandomness random){
        StringBuilder sb = new StringBuilder();
        int length = random.nextInt(MAX_LENGTH_OTHER + 1);
        for (int i = 0; i < length; i++)
            sb.append(random.nextChar('a','z'));
        return sb.toString();
    }
    @Override
    public String generate(SourceOfRandomness random, GenerationStatus status) {
        // generate true strings
        if(random.nextBoolean()){ 
            String trueString = TRUE_STRINGS.get(random.nextInt(TRUE_STRINGS.size()));
            // case mutation does not change "trueness"    
            trueString = mutateCase(trueString,random);
            double randChoice = random.nextDouble();
            if(randChoice < APPEND_RANDOM_PROB)
                return trueString + generateRandomString(random);
            else if(randChoice >= APPEND_RANDOM_PROB && 
                    randChoice < APPEND_RANDOM_PROB + REMOVE_SUFFIX_PROB)
                return removeSuffix(trueString,random);
            else
                return trueString;
        } else { 
            // generate a false string  
            if(random.nextBoolean()){
                String falseString = FALSE_STRINGS.get(random.nextInt(FALSE_STRINGS.size()));
                // case mutation does not change "trueness"    
                falseString = mutateCase(falseString,random);
                if(random.nextDouble() < APPEND_RANDOM_PROB)
                    return falseString + generateRandomString(random);
                else
                    return falseString;
            // null or generate random garbage
            } else {
                if(random.nextDouble() < NULL_PROB){
                    return null;
                }
                else {
                    return generateRandomString(random);
                }
            }
        }
    }
}