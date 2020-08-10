package fieldtest.usage;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.generator.java.lang.AbstractStringGenerator;

import org.jfree.data.xy.XYSeries;
import java.util.List;
import java.util.ArrayList;


public class SimulateUsageGenerator extends Generator<XYSeries> {

    private static final int MAX_NR_ELEMS = 4;
    private static final int MAX_LEN = 10;
    private static final int MAX_VALUE = 10;
    private static final int MAX_TRIES = 10;
    private static final double PROB_UNUSUAL = 0.05;
    public SimulateUsageGenerator() {
        super(XYSeries.class);
    }
    public String generateKeyString(SourceOfRandomness random){
        StringBuilder sb = new StringBuilder();
        int len = random.nextInt(0,MAX_LEN);
        for (int i = 0; i < len; i++){
            sb.append(random.nextChar('a','z'));
        }
        return sb.toString();
    }
    @Override
    public XYSeries generate(SourceOfRandomness random, GenerationStatus status) {
        int tries = MAX_TRIES;
        
        String key = generateKeyString(random);
        boolean autoSort = random.nextBoolean();
        boolean allowDuplX = random.nextBoolean();
        while(tries --> 0){
            try{
                XYSeries series = new XYSeries(key, autoSort, allowDuplX);
                int nrElems = random.nextInt(0,MAX_NR_ELEMS);
                List<Integer> usedXvalues = new ArrayList();
                
                for (int i = 0; i < nrElems; i++){
                    int x = 0;
                    if(i > 0 && random.nextBoolean()){
                        x = usedXvalues.get(random.nextInt(0, usedXvalues.size()-1));
                    } else {
                        x = random.nextInt(0,MAX_VALUE);
                        usedXvalues.add(x); 
                    }
                    if (random.nextBoolean()){ // normal values
                        int y = random.nextInt(0,MAX_VALUE);
                        series.addOrUpdate((double)x, (double)y);
                    } else {
                        // try some unusual values
                        series.addOrUpdate((double)x, random.nextDouble() < PROB_UNUSUAL ? Double.NaN : Double.POSITIVE_INFINITY);
                    }
                }
                
                return series; 
            } catch(Exception e){
                System.err.println("Exception during simulation, tries left: " + tries);
            }
        } 
        return new XYSeries(key, autoSort, allowDuplX);
    }
}
