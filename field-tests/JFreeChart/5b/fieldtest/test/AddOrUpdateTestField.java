package fieldtest.test;

import fieldtest.aspect.TestStorage;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYDataItem;
import junit.framework.TestCase;

public class AddOrUpdateTestField extends TestCase {
	
	private static final double EPSILON = 1e-6;
	
	private boolean containsXY(XYSeries series, double x, double y){
        for (Object item : series.getItems()){
            XYDataItem xyItem = (XYDataItem) item;
            if(Math.abs(xyItem.getX().doubleValue() - x) < EPSILON && Math.abs(xyItem.getY().doubleValue() - y) <EPSILON){
                return true;
            }
        }
        return false;
	}
	
	// Test adapted from XYSeriesTests.testAddOrUpdate3() and testAddOrUpdate2 and testAddOrUpdate
	
    public void testAddOrUpdate() {
        XYSeries series;
		try {
			series = (XYSeries) TestStorage.series.clone();
			int itemCountBefore = series.getItemCount();
            if(itemCountBefore == 0){
                double x = 1.0; 
                double y = 1.0; 
                series.addOrUpdate(x, y);
                assertTrue(series.getItemCount() == 1);
                assertEquals(1.0, series.getY(0).doubleValue(), EPSILON);
            } else {
                double x = series.getX(0).doubleValue();
                double y = 13371337.0; 
                series.addOrUpdate(x, y);
                if(series.getAllowDuplicateXValues()){
                    assertEquals(itemCountBefore + 1, series.getItemCount());
                    if(series.getAutoSort()){
                        assertTrue(containsXY(series,x,y));
                    } else {
                        assertEquals(y, series.getY(itemCountBefore).doubleValue(), EPSILON);
                    }
                } else {
                    assertEquals(itemCountBefore, series.getItemCount());
                    if(series.getAutoSort()){
                        assertTrue(containsXY(series,x,y));
                    } else{
                        // updated in place
                        assertEquals(y, series.getY(0).doubleValue(), EPSILON);
                    }
                }
	        }

		} catch (CloneNotSupportedException e) {
			fail("Could not clone XYSeries, test is broken");
		}      
    }

}
