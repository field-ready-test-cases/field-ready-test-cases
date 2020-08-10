package fieldtest.test;

import fieldtest.aspect.TestStorage;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.Year;
import org.jfree.data.time.TimeSeriesDataItem;

import junit.framework.TestCase;

public class TimeSeriesTestField extends TestCase {
	
	// Adapted from TimeSeriesTests.testClone2()
	
    public void testClone2() {
    	TimeSeries s1 = TestStorage.series;
    	TimeSeries s2 = null;
    	try {
    		s2 = (TimeSeries) s1.clone();
    	}
    	catch (CloneNotSupportedException e) {
    		e.printStackTrace();
    	}
    	assertTrue(s1.equals(s2));
    	
    	if(s2.getItemCount() > 0){
            // check independence
            
            TimeSeriesDataItem lastItem = s2.getDataItem(s2.getItemCount() - 1);
            s2.addOrUpdate(lastItem.getPeriod(), lastItem.getValue().doubleValue() + 1);
            assertFalse(s1.equals(s2));
    	}

    }
}
