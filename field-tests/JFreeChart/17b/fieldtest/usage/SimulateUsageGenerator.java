package fieldtest.usage;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.generator.java.lang.AbstractStringGenerator;


import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.Year;

// copied from generator for Bug 3
public class SimulateUsageGenerator extends Generator<TimeSeries> {

  
    private static final int MAX_LEN_NAME = 5;
    private static final int MAX_ELEMS = 10;
    private static final int FIRST_YEAR = 1990;
    private static final int LAST_YEAR = 2015;
    public SimulateUsageGenerator() {
        super(TimeSeries.class);
    }
    
    
    public String generateTimeSeriesName(SourceOfRandomness random) {
        int len = random.nextInt(0,5);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < len ; i++){
            if(random.nextBoolean()){
                sb.append(random.nextChar('A','Z'));
            } else
                sb.append(random.nextChar('0','9'));
        }
        return sb.toString();
    }
    
    @Override
    public TimeSeries generate(SourceOfRandomness random, GenerationStatus status) {
        TimeSeries series = new TimeSeries(generateTimeSeriesName(random));
        int nrElems = random.nextInt(0, MAX_ELEMS);
        for(int i = 0; i < nrElems; i++){
            series.addOrUpdate(new Year(random.nextInt(FIRST_YEAR,LAST_YEAR)), random.nextDouble() * 100);
        }
        
        return series; 
    }
}
