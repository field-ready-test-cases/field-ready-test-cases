package fieldtest.usage;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.generator.java.lang.AbstractStringGenerator;

public class SimulateUsageGenerator extends Generator<String> {
    // generate some hex with a some boundary cases as well
    // i.e., 0x80... and 0x7F...
    private static final String LOWERCASE_CHARS = "abcdef";
    private static final String UPPERCASE_CHARS = "ABCDEF";
    private static final String NUMBERS = "0123456789";
    private static final String UPPERCASE_DIGITS = UPPERCASE_CHARS
          + NUMBERS;
    private static final String LOWERCASE_DIGITS = LOWERCASE_CHARS
          + NUMBERS;

    public static final int MAX_LENGTH = 16;
    public static final double NON_HEX_PROBABILITY = 0.975;

    public SimulateUsageGenerator() {
        super(String.class);
    }

    @Override
    public String generate(SourceOfRandomness random, GenerationStatus status) {
        StringBuilder sb = new StringBuilder();
        if(random.nextDouble() < NON_HEX_PROBABILITY){ // generate  normal integers in most cases
            return Integer.toString(random.nextInt());
        }
        boolean uppercase = random.nextBoolean();
        if(random.nextBoolean()) 
            sb.append(uppercase ? "0X" : "0x");
        else
            sb.append(uppercase ? "-0X" : "-0x");
        
            
        int length=random.nextInt(MAX_LENGTH); // allow length of zero
        
         // generate hex boundary values, i.e., 0x80... or 0x7F...        
        if(length >= 2 && random.nextBoolean()){
            // 0x80...
            if(random.nextBoolean()){
                sb.append('8');
                while(length > 1){
                    sb.append('0');
                    length --;
                }
            } else { // 0x7F...
                sb.append('7');
                while(length > 1){
                    sb.append('F');
                    length --;
                }
            }
        }
        else { // generate any hex value
            for (int i = 0; i < length; i++) {
                int randomIndex = random.nextInt(UPPERCASE_DIGITS.length());
                if(uppercase)
                    sb.append(UPPERCASE_DIGITS.charAt(randomIndex));
                else
                    sb.append(LOWERCASE_DIGITS.charAt(randomIndex));
            }
        }
           
        return sb.toString();
    }
}
