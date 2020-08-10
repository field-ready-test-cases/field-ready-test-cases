package fieldtest.usage;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.generator.java.lang.AbstractStringGenerator;

public class SimulateUsageGenerator extends Generator<StrBuilderDeleteArgs> {
    // generate some arguments for StrBuilder.deleteAll

    public static final int MAX_LENGTH_RAND = 20;

    public static final double NEG_CASE_PROBABILITY = 0.2;
    public static final double WHITE_SPACE_PROBABILITY = 0.1;
    public static final double INSERT_STOP_PROBABILITY = 0.5;

    public SimulateUsageGenerator() {
        super(StrBuilderDeleteArgs.class);
    }
    private String generateRandomString(SourceOfRandomness random, int length){
        return generateRandomStringBuilder(random,length).toString();
    }
    private StringBuilder generateRandomStringBuilder(SourceOfRandomness random, int length){
        StringBuilder sb = new StringBuilder(); 
        for(int i = 0; i < length; i++){
            if(random.nextDouble() < WHITE_SPACE_PROBABILITY){
                sb.append(random.nextBoolean() ? ' ' : '\n');
            } else {
                sb.append(random.nextChar('a','z'));
            }
        }
        return sb;
    }
    
    @Override
    public StrBuilderDeleteArgs generate(SourceOfRandomness random, GenerationStatus status) {
        StrBuilderDeleteArgs args = new StrBuilderDeleteArgs();
        // generate some cases where the stringbuilder should be left unchanged
        if(random.nextDouble() < NEG_CASE_PROBABILITY){
            // allow zero
            int lengthComplete = random.nextInt(MAX_LENGTH_RAND + 1); 
            args.strBuilderString = generateRandomString(random, lengthComplete);
            switch(random.nextInt(3)){
                case 0: 
                    args.toDelete = null;
                    break;
                case 1:
                    args.toDelete = "";
                    break;
                case 2:
                    int lengthToDelete = random.nextInt(MAX_LENGTH_RAND + 1);
                    // unlikely to generate a substring
                    args.toDelete = generateRandomString(random, lengthToDelete);
                    break;
            }
        } else {
            // let's do some easy one in the then branch and harder ones in the 
            // else branch
            // easy: strBuilderString is random and deletion string is a random
            // non-empty substring, which is unlikely to repeat
            // hard: strBuilderString is random, but we insert the deletion 
            // string and prefixes of it multiple times into strBuilderString
            // (likely at start or end)
            
            // don't allow zero            
            int lengthComplete = 1 + random.nextInt(MAX_LENGTH_RAND); 
            StringBuilder complete = generateRandomStringBuilder(random, lengthComplete);
            if(random.nextBoolean()){
                args.strBuilderString = complete.toString();
                int startIndexSubstring = random.nextInt(0,
                                              args.strBuilderString.length()-1);
                int endIndexSubstring = random.nextInt(startIndexSubstring + 1, 
                                                args.strBuilderString.length());
                args.toDelete = args.strBuilderString
                            .substring(startIndexSubstring, endIndexSubstring);
            } else {
                // length > 2 to have proper prefixes  
                int lengthSubString = 2 + random.nextInt(lengthComplete); 
                String deleteString = generateRandomString(random,lengthSubString);
                do {
                    String insertString;
                    // do a prefix
                    if(random.nextBoolean()){
                        // random.nextInt \in [0,length-1) -> endIndex of substring is exclusive so 
                        // we get a proper prefix
                        insertString = deleteString.substring(0, random.nextInt(deleteString.length()));
                    } else {
                        insertString = deleteString;
                    }
                    // insert either at beginning (25%), a random place (50%), or at end (25%)
                    switch(random.nextInt(4)){
                        case 0:
                            complete.insert(0, insertString);
                            break;
                        case 1:
                        case 2:
                            complete.insert(random.nextInt(complete.length() + 1), insertString);
                            break;
                        case 3:
                        default:
                            complete.append(insertString);
                            break;
                    }
                } while(random.nextDouble() < INSERT_STOP_PROBABILITY);

                args.toDelete = deleteString;
                args.strBuilderString = complete.toString();
            }
        }

        return args;
    }
}
