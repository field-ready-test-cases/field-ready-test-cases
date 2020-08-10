package fieldtest.test;

import fieldtest.aspect.TestStorage;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.time.TimePeriodValues;
import org.jfree.data.time.TimePeriod;
import java.util.Date;

import junit.framework.TestCase;

public class MaxMiddleIndexTestField extends TestCase {
	
	private long computeMiddleTime(TimePeriod tp){
        return tp.getStart().getTime() + (tp.getEnd().getTime() - tp.getStart().getTime())/2;
	}
	
	// adapted testGetMinMiddleIndex() for the case of maximum instead of minimum, 
	// So we check whether the middle point of the time period at the returned maximum index is the largest
	// instead of the smallest
    public void testGetMaxMiddleIndex() {
        TimePeriodValues s = TestStorage.tpv;
        org.junit.Assume.assumeNotNull(s);

//         try{
            // we would like to clone, but we get an IndexOutOfBoundsException if we do that
            //TimePeriodValues testValues = (TimePeriodValues)TestStorage.tpv.clone();
        TimePeriodValues testValues = (TimePeriodValues)TestStorage.tpv;
        int maxMiddleIndex = s.getMaxMiddleIndex();
        if(testValues.getItemCount() == 0){
            assertEquals(-1 , s.getMaxMiddleIndex());
        } else{
            TimePeriod maxMiddleTP = testValues.getDataItem(maxMiddleIndex).getPeriod();
            long maxMiddle = computeMiddleTime(maxMiddleTP);
            for(int i = 0; i < testValues.getItemCount(); i++){
                assertTrue("Max middle should be at least as large as the middle of all time periods",
                    maxMiddle >= computeMiddleTime(testValues.getDataItem(i).getPeriod()));
            }
        }
//         } catch(CloneNotSupportedException e) {
//             // assume that cloning is supported
//             org.junit.Assume.assumeTrue(false);
//         }
    }
}
