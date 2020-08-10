package fieldtest.usage;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;
import com.pholser.junit.quickcheck.From;
import static org.junit.Assert.*;

import org.junit.BeforeClass;

import org.jfree.data.category.DefaultIntervalCategoryDataset;

import fieldtest.triggering.TriggerSingleton;
import fieldtest.triggering.static_anomaly_detection.StaticAnomalyDetectionTrigger;
import fieldtest.triggering.simple.AllTestTrigger;

@RunWith(JUnitQuickcheck.class)
public class UseWithQuickCheck {
// 	@BeforeClass
	public static void initSimulation() {
		int maxDepth = 6;
		boolean exploreArrays = true;
		boolean allowMultipleCoverage = false;
		//TriggerSingleton.init(new StaticAnomalyDetectionTrigger(maxDepth, 
		//		exploreArrays,allowMultipleCoverage));	
        TriggerSingleton.init(new AllTestTrigger());
	}
    @Property(trials =1000) public void simulateUsageCreateNumber(@From(SimulateUsageGenerator.class) 
        DefaultIntervalCategoryDataset ds) {
        try{
            System.out.println(ds.getCategoryCount());
        } catch(Throwable t){
            // ignore exceptions in simulation, as errors will be logged by field
        }
    }
}