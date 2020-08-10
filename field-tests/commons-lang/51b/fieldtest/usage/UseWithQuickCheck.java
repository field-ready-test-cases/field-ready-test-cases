package fieldtest.usage;

import org.apache.commons.lang.BooleanUtils;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;
import com.pholser.junit.quickcheck.From;
import static org.junit.Assert.*;

@RunWith(JUnitQuickcheck.class)
public class UseWithQuickCheck {
    @Property(trials = 500) public void simulateUsageToBoolean(@From(SimulateUsageGenerator.class) String s) {
        System.out.println(s);
        try{
			BooleanUtils.toBoolean(s);
        } catch(Throwable t){
            // ignore exceptions in simulation, as errors will be logged by field
        }
    }
}
