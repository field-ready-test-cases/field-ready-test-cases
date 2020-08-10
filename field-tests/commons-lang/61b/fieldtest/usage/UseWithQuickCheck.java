package fieldtest.usage;

import org.apache.commons.lang.text.StrBuilder;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;
import com.pholser.junit.quickcheck.From;
import static org.junit.Assert.*;

@RunWith(JUnitQuickcheck.class)
public class UseWithQuickCheck {
    // this might require a few more trials
    @Property(trials = 1000) public void simulateUsageDeleteAll(@From(SimulateUsageGenerator.class) StrBuilderDeleteArgs args) {
        System.out.println(args.strBuilderString);
        try{
            StrBuilder sb = new StrBuilder(args.strBuilderString);
            sb.deleteAll(args.toDelete);
        } catch(Throwable t){
            // ignore exceptions in simulation, as errors will be logged by field
        }
    }
}
