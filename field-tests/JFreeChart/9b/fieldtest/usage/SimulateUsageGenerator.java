package fieldtest.usage;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.generator.java.lang.AbstractStringGenerator;

import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.Day;


public class SimulateUsageGenerator extends Generator<CopyArgs> {

    private static final int MAX_NR_ELEMS = 5;
    private static final int FIRST_YEAR = 2003;
    private static final int LAST_YEAR = 2008;
    private static final double PROB_USE_TWICE = 0.025;
    
    public SimulateUsageGenerator() {
        super(CopyArgs.class);
    }
    
    Day generateDay(SourceOfRandomness random){
        // just create valid days for sure by restricting to 1 to 28
        return new Day(random.nextInt(1,28), random.nextInt(1,12), random.nextInt(FIRST_YEAR, LAST_YEAR));
    }
    
    @Override
    public CopyArgs generate(SourceOfRandomness random, GenerationStatus status) {
        CopyArgs args = new CopyArgs();
        
        TimeSeries series = new TimeSeries("Test-Series");
        int nrElems = random.nextInt(0,MAX_NR_ELEMS);
        for(int i = 0; i < nrElems; i++){
            series.addOrUpdate(generateDay(random), random.nextDouble() * 100);
        }
        args.series = series;
        Day first = generateDay(random);
        Day second = random.nextDouble()  < PROB_USE_TWICE ? first : generateDay(random);
        args.start = first;
        args.end = second;
        return args; 
    }
}
