package fieldtest.usage;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.generator.java.lang.AbstractStringGenerator;

import org.jfree.data.category.DefaultIntervalCategoryDataset;


public class SimulateUsageGenerator extends Generator<DefaultIntervalCategoryDataset> {

    private static final int MAX_SIZE = 4;
    private static final int MAX_STRING_LENGTH = 10;
    private static final int MAX_VALUE = 10;
    
    public SimulateUsageGenerator() {
        super(DefaultIntervalCategoryDataset.class);
    }
    
    public String generateString(SourceOfRandomness random){
        int len = random.nextInt(0,MAX_STRING_LENGTH);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < len; i++){
            if(random.nextBoolean()){
                sb.append(random.nextChar('0','9'));
            } else {
                sb.append(random.nextChar('a','z'));
            }       
        }        
        return sb.toString();
    }
    public Number generateNumber(SourceOfRandomness random, boolean createInts){
        if(createInts)
            return random.nextInt(-MAX_VALUE,MAX_VALUE);
        else
            return random.nextDouble()*2*MAX_VALUE-MAX_VALUE;
    }
    
    @Override
    public DefaultIntervalCategoryDataset generate(SourceOfRandomness random, GenerationStatus status) {
     
        DefaultIntervalCategoryDataset ds = null; 
        Number[][] starts = null;
        Number[][] ends = null;
        int nrSeries = random.nextInt(0,MAX_SIZE); // allow empty
        int nrCategories = random.nextInt(0,MAX_SIZE);
        // use one of three constructors (ctors with double and number parameters are the same)
        int ctorChoice = random.nextInt(3); 
        
        starts = new Number[nrSeries][nrCategories];
        ends = new Number[nrSeries][nrCategories];
        boolean createInts = random.nextBoolean();
        for(int s = 0; s < nrSeries; s++){
            for(int c = 0; c < nrCategories;c++){
                starts[s][c] = generateNumber(random, createInts);
                ends[s][c] = generateNumber(random, createInts);
            }
        }
        
        if(ctorChoice == 0){
            return new DefaultIntervalCategoryDataset(starts,ends);
        } else {
            String[] seriesNames = new String[nrSeries];
            for(int s = 0; s < nrSeries; s++){
                seriesNames[s] = generateString(random);
            }
            if(ctorChoice == 1){
                return new DefaultIntervalCategoryDataset(seriesNames,starts,ends);
            } else{
                String[] categoryNames = new String[nrCategories];
                for(int c = 0; c < nrCategories; c++){
                    categoryNames[c] = generateString(random);
                }
                return new DefaultIntervalCategoryDataset(seriesNames,categoryNames,starts,ends);
            }
        }
    }
}
