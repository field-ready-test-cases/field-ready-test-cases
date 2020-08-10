package fieldtest.usage;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.generator.java.lang.AbstractStringGenerator;

public class SimulateUsageGenerator extends Generator<String> {
    // generate some text that could be contained within XML text
    public static final int MAX_LENGTH = 4; // generate short strings
    public static final double PROB_SUPPL_PLANE = 0.05; 

    public SimulateUsageGenerator() {
        super(String.class);
    }

    private void encloseInTag(StringBuilder sb, SourceOfRandomness random){
        StringBuilder tagText = new StringBuilder();
        int tagLen = random.nextInt(MAX_LENGTH+1);
        for(int i = 0; i < tagLen; i ++)
            tagText.append(random.nextChar('a','z'));
        sb.insert(0, "<" + tagText.toString() + ">");
        sb.append("</" + tagText.toString() + ">");
    }
    private void addQuotesOrAmpersands(StringBuilder sb, SourceOfRandomness random){
        char toInsert = 'a';
        switch(random.nextInt(3)){
            case 0: // insert "
                toInsert = '"';
                break;
            case 1: // insert '
                toInsert = '\'';
                break;
            case 2: // insert &
            default:
                toInsert = '&';
                break;
        }
        sb.insert(random.nextInt(sb.length() + 1), toInsert);
        sb.insert(random.nextInt(sb.length() + 1), toInsert);
    }
    @Override
    public String generate(SourceOfRandomness random, GenerationStatus status) {
        StringBuilder sb = new StringBuilder();
        int length = random.nextInt(MAX_LENGTH+1);
        for(int i = 0; i < length; i ++){
             // randomly add character from supplementary plane
            if(random.nextDouble() < PROB_SUPPL_PLANE)
                sb.append("\ud842\udfb7");
            else // latin characters plus some characters not escaped in XML
                sb.append(random.nextChar('A','z'));
        }
        if(random.nextBoolean()){
            addQuotesOrAmpersands(sb,random);
        }
        if(random.nextBoolean()){
            encloseInTag(sb,random);
        }
           
        return sb.toString();
    }
}
