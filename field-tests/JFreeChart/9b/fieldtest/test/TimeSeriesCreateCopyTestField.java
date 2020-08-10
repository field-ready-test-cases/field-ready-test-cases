package fieldtest.test;

import fieldtest.aspect.TestStorage;
import org.jfree.data.time.TimeSeries;

import junit.framework.TestCase;

public class TimeSeriesCreateCopyTestField extends TestCase {
	
	
	// Adapted from TimeSeriesTests.testCreateCopy1() and testCreateCopy2
	public void testCreateCopy() {

        TimeSeries series = TestStorage.series;
        try {
            TimeSeries result1 = series.createCopy(TestStorage.start, TestStorage.end);
            assertNotNull(result1);
        }catch (CloneNotSupportedException e) {
            assertTrue(false);
        } catch(IllegalArgumentException e){
            // illegal argument exception is fine if end > start 
            assertTrue(TestStorage.start.compareTo(TestStorage.end) > 0);
        }
	}
        
        
        
}
