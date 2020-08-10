package fieldtest.usage;

import org.apache.commons.lang.text.StrBuilder;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;
import com.pholser.junit.quickcheck.From;
import static org.junit.Assert.*;

@RunWith(JUnitQuickcheck.class)
public class UseWithQuickCheck {
    @Property(trials =1000) public void simulateUsageCreateNumber(@From(SimulateUsageGenerator.class) PadArguments args) {
        try{
			StrBuilder sb = new StrBuilder(args.s);
			sb.appendFixedWidthPadRight(args.obj, args.width, args.padChar);
        } catch(Throwable t){
            // ignore exceptions in simulation, as errors will be logged by field
        }
    }
}
