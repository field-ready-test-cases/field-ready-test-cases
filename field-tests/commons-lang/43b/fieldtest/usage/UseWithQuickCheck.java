package fieldtest.usage;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;
import com.pholser.junit.quickcheck.From;
import static org.junit.Assert.*;
import org.apache.commons.lang.text.ExtendedMessageFormat;

@RunWith(JUnitQuickcheck.class)
public class UseWithQuickCheck {
    @Property(trials=10) public void simulateEMFFormat(@From(SimulateUsageGenerator.class) EMFArgs args) {
        try{
	        ExtendedMessageFormat emf = new ExtendedMessageFormat(args.pattern, args.registry);
        } catch(Throwable t){
            // ignore exceptions in simulation, as errors will be logged by field
        }
    }
}
