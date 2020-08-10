package fieldtest.usage;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.generator.java.lang.AbstractStringGenerator;

public class SimulateUsageGenerator extends Generator<ReplaceArgs> {
    // generate some arguments for StringUtils.replaceEach

    public static final int MAX_LENGTH_TEXT = 20;

    public static final double NULL_PROBABILITY = 0.05;
    public static final double NULL_ELEMENT_PROBABILITY = 0.05;
    // there is no white space in the in-house test case, but we should have a low amount
    public static final double WHITE_SPACE_PROBABILITY = 0.05; 
    public static final double LONG_SEARCH_STRING_PROBABILITY = 0.25; 
    
    public static final int MAX_LIST_SIZE = 3;
    private static final char FIRST_CHAR = 'a';
    private static final char LAST_CHAR = 'e';
    
    
    public SimulateUsageGenerator() {
        super(ReplaceArgs.class);
    }
    private String generateRandomString(SourceOfRandomness random, int length){
        return generateRandomStringBuilder(random,length).toString();
    }
    private StringBuilder generateRandomStringBuilder(SourceOfRandomness random, int length){
        StringBuilder sb = new StringBuilder(); 
        for(int i = 0; i < length; i++){
            if(random.nextDouble() < WHITE_SPACE_PROBABILITY){
                sb.append(' ');
            } else {
                // 
                sb.append(random.nextChar(FIRST_CHAR,LAST_CHAR));
            }
        }
        return sb;
    }
    
    @Override
    public ReplaceArgs generate(SourceOfRandomness random, GenerationStatus status) {
        ReplaceArgs args = new ReplaceArgs();
        int length = random.nextInt(MAX_LENGTH_TEXT + 1);
        String text = generateRandomString(random, length);
        int listLength = random.nextInt(MAX_LIST_SIZE + 1); // allow 0
        String [] searchList = new String[listLength];
        String [] replacementList = new String[listLength];
        
        for(int i = 0; i < listLength; i++){
            // values in replacementList largely irrelevant, use only short ones
            int replacementLength = random.nextInt(0,2);
            replacementList[i] = generateRandomString(random,replacementLength);
            
            // for search elems, three options: element of replacementList, random, or a substring 
            // of text
            switch(random.nextInt(3)){
                case 0:
                    if(i > 0){
                        searchList[i] = replacementList[random.nextInt(0,i)];
                        break;
                    } // if there is no element in replacementList -> default to case 1 (random)
                case 1:
                    int searchLength = random.nextInt(0,2); 
                    searchList[i] = generateRandomString(random,searchLength);
                    break;
                case 2:
                default:
                    // use a substring of text
                    if(text.isEmpty())
                        searchList[i] = "";
                    else {
                        int startIndex = random.nextInt(0, text.length()-1);
                        int endIndex = random.nextInt(startIndex+1, text.length());
                        // probability of short search strings >= 1-LONG_SEARCH_STRING_PROBABILITY
                        if(endIndex - startIndex > 2 && 
                                            random.nextDouble() >= LONG_SEARCH_STRING_PROBABILITY){
                            // reduce length substring length
                            endIndex = startIndex + 2;
                        }
                        searchList[i] = text.substring(startIndex, endIndex);
                    }
                    break;
            }
        
            if(random.nextDouble() < NULL_ELEMENT_PROBABILITY){
                if(random.nextBoolean()){
                    searchList[i] = null;
                } else {
                    replacementList[i] = null;
                }
            }
        }
        
        args.text = text;
        args.searchList = searchList;
        args.replaceList = replacementList;
        // set one of the args to null
        if(random.nextDouble() < NULL_PROBABILITY){
            switch(random.nextInt(3)){
                case 0:
                    args.text = null;
                    break;
                case 1:
                    args.searchList = null;
                    break;
                case 2:
                default:
                    args.replaceList = null;
                    break;
            }
        }
        return args;
    }
}
