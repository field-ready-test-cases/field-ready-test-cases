package fieldtest.usage;

import org.apache.commons.lang3.StringUtils;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;
import com.pholser.junit.quickcheck.From;
import static org.junit.Assert.*;

@RunWith(JUnitQuickcheck.class)
public class UseWithQuickCheck {
    // this might require a few more trials
    @Property(trials = 1000) public void simulateReplaceEach(@From(SimulateUsageGenerator.class) ReplaceArgs args) {
        try{
            StringUtils.replaceEach(args.text,args.searchList,args.replaceList);
        } catch(Throwable t){
            // ignore exceptions in simulation, as errors will be logged by field
        }
    }
}
