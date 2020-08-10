package fieldtest.usage;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.generator.java.lang.AbstractStringGenerator;

import org.jfree.data.time.TimePeriodValues;
import org.jfree.data.time.SimpleTimePeriod;

public class SimulateUsageGenerator extends Generator<TimePeriodValues> {

    private static final int MAX_NR_TIMEPERIODS = 5;
    private static final double SPECIAL_VALUE_PROBABILITY = 0.025;
    private static final int FIRST_POSSIBLE_TIME = 100;
    private static final int LAST_POSSIBLE_TIME = 1000;
    
    public SimulateUsageGenerator() {
        super(TimePeriodValues.class);
    }
    public long generateTimeValue(SourceOfRandomness random){
        if(random.nextDouble() < SPECIAL_VALUE_PROBABILITY){
            switch(random.nextInt(5)){
                case 0:
                    return -1l;
                case 1:
                    return Long.MIN_VALUE;
                case 2:
                    return Long.MAX_VALUE;
                case 3:
                    return Integer.MAX_VALUE;
                case 4:
                default:
                    return 0;
            }
        } else {
            return random.nextInt(100,1000);
        }
    }
    @Override
    public TimePeriodValues generate(SourceOfRandomness random, GenerationStatus status) {
        TimePeriodValues tpv = new TimePeriodValues("test_tpv");
        int nrTps = random.nextInt(0,MAX_NR_TIMEPERIODS);
        for(int i = 0; i < nrTps; i ++){
            long first = generateTimeValue(random);
            long second = generateTimeValue(random);
            
            tpv.add(new SimpleTimePeriod(Math.min(first,second),Math.max(first,second)),random.nextDouble());
        }
        return tpv; 
    }
}
