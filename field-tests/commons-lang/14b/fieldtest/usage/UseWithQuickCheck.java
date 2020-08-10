package fieldtest.usage;

import org.apache.commons.lang3.StringUtils;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;
import com.pholser.junit.quickcheck.From;
import static org.junit.Assert.*;

@RunWith(JUnitQuickcheck.class)
public class UseWithQuickCheck {
    @Property(trials=1000) public void simulateStringUtilsEquals(@From(SimulateUsageGenerator.class) CharSequencePair s) {
        System.out.println(s.left + " == " + s.right);
        try{
     	    System.out.println(StringUtils.equals(s.left,s.right));
	    } catch(Throwable t){
            // ignore exceptions in simulation, as errors will be logged by field
        }
    }
}