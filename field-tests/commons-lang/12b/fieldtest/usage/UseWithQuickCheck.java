package fieldtest.usage;

import org.apache.commons.lang3.math.NumberUtils;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;
import com.pholser.junit.quickcheck.From;
import static org.junit.Assert.*;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;

@RunWith(JUnitQuickcheck.class)
public class UseWithQuickCheck {
    @Property(trials=1000) public void simulateUsageRandomStringUtils(@From(SimulateUsageGenerator.class) RandomStringUtilsArguments args) {
        long seed = System.currentTimeMillis();
        try{
            RandomStringUtils.random(args.count,args.start,args.end,
                args.letters,args.numbers,args.characters,new Random(seed));
        } catch(Throwable t){
            // ignore exceptions in simulation, as errors will be logged by field
        }
    }
}
