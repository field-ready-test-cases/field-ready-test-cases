package fieldtest.usage;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.generator.java.lang.AbstractStringGenerator;

public class SimulateUsageGenerator extends Generator<String> {

    // generate some small hex values potentially uppercase X
    private static final String LOWERCASE_CHARS = "abcdef";
    private static final String UPPERCASE_CHARS = "ABCDEF";
    private static final String NUMBERS = "0123456789";
    private static final String UPPERCASE_DIGITS = UPPERCASE_CHARS
          + NUMBERS;
    private static final String LOWERCASE_DIGITS = LOWERCASE_CHARS
          + NUMBERS;

    public static final int MAX_LENGTH = 6;

    public SimulateUsageGenerator() {
        super(String.class);
    }

    @Override
    public String generate(SourceOfRandomness random, GenerationStatus status) {
        StringBuilder sb = new StringBuilder();
        if(random.nextBoolean())
            sb.append("0x");
        else
            sb.append("0X"); // try uppercase X as well
        int length=random.nextInt(MAX_LENGTH); // allow length of zero
        
        // generate any hex value
        boolean uppercase = random.nextBoolean();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(UPPERCASE_DIGITS.length());
            if(uppercase)
                sb.append(UPPERCASE_DIGITS.charAt(randomIndex));
            else
                sb.append(LOWERCASE_DIGITS.charAt(randomIndex));
        }
    
           
        return sb.toString();
    }
}