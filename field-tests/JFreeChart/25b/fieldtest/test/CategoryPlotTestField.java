package fieldtest.test;



import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import fieldtest.aspect.TestStorage;

import junit.framework.TestCase;

public class CategoryPlotTestField extends TestCase {
	
	// Adapted from StatisticalBarRendererTests.testDrawWithNullInfo()
	public void testDrawWithNullInfo() {
        boolean success = false;
        CategoryPlot plot = TestStorage.plot;
        try {
            JFreeChart chart = new JFreeChart(plot);
            chart.createBufferedImage(300, 200, null);
            success = true;
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            success = false;
        }
        assertTrue(success);
    }
    
}
