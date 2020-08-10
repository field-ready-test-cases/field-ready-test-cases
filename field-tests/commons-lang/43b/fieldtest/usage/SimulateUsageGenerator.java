package fieldtest.usage;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.generator.java.lang.AbstractStringGenerator;

public class SimulateUsageGenerator extends Generator<EMFArgs> {
    /**
    * Format factories taken from original unit test case. They do not matter too much for the field
    * test. However, we will add some variation.
    * 
    *
    */
    private static class LowerCaseFormat extends Format {
        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            return toAppendTo.append(((String)obj).toLowerCase());
        }
        public Object parseObject(String source, ParsePosition pos) {throw new UnsupportedOperationException();}
    }
    private static class UpperCaseFormat extends Format {
        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            return toAppendTo.append(((String)obj).toUpperCase());
        }
        public Object parseObject(String source, ParsePosition pos) {throw new UnsupportedOperationException();}
    }
    public static class LowerCaseFormatFactory implements FormatFactory {
        private static final Format LOWER_INSTANCE = new LowerCaseFormat();
        public Format getFormat(String name, String arguments, Locale locale) {
            return LOWER_INSTANCE;
        }
    }
    public static class UpperCaseFormatFactory implements FormatFactory {
        private static final Format UPPER_INSTANCE = new UpperCaseFormat();
        public Format getFormat(String name, String arguments, Locale locale) {
            return UPPER_INSTANCE;
        }
    }
    /**
    * End of formats implementation
    */
    
    private StringBuilder generateRandomString(SourceOfRandomness random){
        StringBuilder randomString = new StringBuilder();
        // create a random string
        int length = random.nextInt(MIN_LENGTH,MAX_LENGTH);
        for(int i = 0; i < length; i++){
            switch(random.nextInt(4)){
                case 0:
                    randomString.append(random.nextChar('a','z'));
                     break;
                case 1:
                    randomString.append(random.nextChar('A','Z'));
                    break;
                case 2:
                    randomString.append(random.nextChar('0','9'));
                    break;
                case 3:
                    randomString.append(' ');
                    break;
            }
        }    
        return randomString;
    }
    private Map generateRegistry(SourceOfRandomness random){
        if(random.nextDouble() < NULL_PROB_REG){
            return null;
        }
        Map registry = new HashMap(4);
        if(random.nextBoolean())
            registry.put("lower", random.nextDouble() < NULL_PROB_REG ? null: new LowerCaseFormatFactory());
        if(random.nextBoolean())
            registry.put("upper", random.nextDouble() < NULL_PROB_REG ? null: new UpperCaseFormatFactory());
        return registry;
    }
    private void addQuotes(StringBuilder pattern, SourceOfRandomness random){
        int nrQuotes = random.nextInt(1, MAX_NR_QUOTES);
        
        for(int i = 0; i <nrQuotes; i++){
            if(random.nextBoolean()){
                pattern.insert(random.nextInt(0, pattern.length()), "'");
            }
            else {
                pattern.insert(random.nextInt(0, pattern.length()), "''");
            }
        }
    }
    private void addFormatString(StringBuilder pattern, SourceOfRandomness random){
        if(random.nextBoolean())
            return; // do not add format string
        int argPosition = random.nextInt(0,2);
        String formatString = "{" + argPosition + ",";
        
        if(random.nextBoolean()){
            formatString = formatString + "lower}";
        } else {
            formatString = formatString + "upper}";
        }
        pattern.insert(random.nextInt(0, pattern.length()), formatString);
    }
    
    public static final double NULL_PROB_REG = 0.1;
    public static final int MAX_LENGTH = 20;
    public static final int MIN_LENGTH = 10;
    public static final int MAX_NR_QUOTES = 6;

    public SimulateUsageGenerator() {
        super(EMFArgs.class);
    }

    @Override
    public EMFArgs generate(SourceOfRandomness random, GenerationStatus status) {
        StringBuilder pattern = generateRandomString(random);
        addQuotes(pattern,random);
        addFormatString(pattern,random);

        EMFArgs args = new EMFArgs();
        args.pattern = pattern.toString();
        args.registry = generateRegistry(random);
           
        return args;
    }
}
