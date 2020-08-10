package fieldtest.test;

import org.jfree.data.category.DefaultIntervalCategoryDataset;
import fieldtest.aspect.TestStorage;

import junit.framework.TestCase;

public class DefaultIntervalCategoryDatasetTestField extends TestCase {
	
	// Adapted from DefaultIntervalCategoryDataset.testCloning()
	
    public void testCloning() {

        DefaultIntervalCategoryDataset d1 = TestStorage.dataset;
        DefaultIntervalCategoryDataset d2 = null;
		try {
			d2 = (DefaultIntervalCategoryDataset) d1.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
        
		assertTrue(d1 != d2);
        assertTrue(d1.getClass() == d2.getClass());
        assertTrue(d1.equals(d2));
        
    }
}
