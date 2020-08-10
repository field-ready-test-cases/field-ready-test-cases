package fieldtest.test;

import junit.framework.TestCase;

import static org.junit.Assume.assumeNotNull;
import static org.junit.Assume.assumeTrue;

import org.jfree.data.Range;
import org.jfree.data.general.DatasetUtilities;
import fieldtest.aspect.TestStorage;
import org.jfree.data.xy.XYDataset;

public class DatasetUtilitiesTestField extends TestCase {

	private boolean containsXValue(){
        for (int i = 0; i < TestStorage.dataset.getSeriesCount();i++){
            for (int j = 0; j < TestStorage.dataset.getItemCount(i);j++){
                if(!Double.isNaN(TestStorage.dataset.getXValue(i,j))){
                    return true;
                }
            }
        }
        return false;
	}
	private boolean containsYValue(){
        for (int i = 0; i < TestStorage.dataset.getSeriesCount();i++){
            for (int j = 0; j < TestStorage.dataset.getItemCount(i);j++){
                if(!Double.isNaN(TestStorage.dataset.getYValue(i,j)))
                    return true;
            }
        }
        return false;
	}
    // Adapted from DatasetUtilitiesTests.testFindDomainBounds*() and testFindDomainBounds_NaN()
    public void testFindDomainBounds() {   	
    
        // test cases also have at least one x and one y value
        assumeTrue(containsXValue());
        assumeTrue(containsYValue());
        XYDataset dataset = TestStorage.dataset;
        Range r = DatasetUtilities.iterateDomainBounds(dataset);
      
        assertNotNull(r);
        assertNotNull(r.getLowerBound());
        assertNotNull(r.getUpperBound());
        assertTrue(r.getLowerBound() <= r.getUpperBound());
        r = DatasetUtilities.iterateRangeBounds(dataset);
    
        assertNotNull(r);
        assertNotNull(r.getLowerBound());
        assertNotNull(r.getUpperBound());
        assertTrue(r.getLowerBound() <= r.getUpperBound());   
    }

}
