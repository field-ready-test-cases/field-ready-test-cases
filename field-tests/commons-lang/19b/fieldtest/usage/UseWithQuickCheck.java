package fieldtest.usage;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;
import com.pholser.junit.quickcheck.From;
import static org.junit.Assert.*;

@RunWith(JUnitQuickcheck.class)
public class UseWithQuickCheck {
    
    @Property(trials=1000) public void simulateUnescapeEntity(@From(SimulateUsageGenerator.class) String s) {
        System.out.println(s);
        try{
            NumericEntityUnescaper neu = new NumericEntityUnescaper();	
			neu.translate(s);
        } catch(Throwable t){
            // ignore exceptions in simulation, as errors will be logged by field
        }
    }
}
