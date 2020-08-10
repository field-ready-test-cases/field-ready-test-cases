package fieldtest.test;

import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import fieldtest.aspect.TestStorage;

import junit.framework.TestCase;

public class SetNullRendererTestField extends TestCase {
    
    // Test adapted from XYPlotTests.testSetNullRenderer()
    public void testSetNullRenderer() {
        JFreeChart chart = TestStorage.chart;
        org.junit.Assume.assumeNotNull(chart);
        org.junit.Assume.assumeTrue(chart.getPlot() instanceof XYPlot);
        XYPlot plot = (XYPlot)chart.getPlot();
        boolean failed = false;
        XYItemRenderer renderer = plot.getRenderer();
        try {
            plot.setRenderer(null);
        }
        catch (Exception e) {
            failed = true;
        }
        assertTrue(!failed);
        plot.setRenderer(renderer);    
    }
}
