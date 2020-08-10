package fieldtest.usage;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultIntervalXYDataset;
import org.jfree.data.general.WaferMapDataset;
import org.jfree.chart.ChartFactory;

public class SimulateUsageGenerator extends Generator<JFreeChart> {

    private static final double PROBABILITY_ADD_DATA = 0.7;
    private static final int MAX_STR_LEN = 10;
    private static final int MAX_CHIPSIZE = 10;
    private static final int MAX_TRIES = 5;
    
    public SimulateUsageGenerator() {
        super(JFreeChart.class);
    }
    public String generateString(SourceOfRandomness random, GenerationStatus status) {
        int len = random.nextInt(1,MAX_STR_LEN);
        
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < len;i++){
            switch(random.nextInt(3)) {
                case 0:
                   sb.append(random.nextChar('0','9')); 
                break;
                case 1:
                   sb.append(random.nextChar('A','Z'));
                break;
                case 2:
                default:
                   sb.append('_');
                break;                
            }
        }
        return sb.toString();
    }
    @Override
    public JFreeChart generate(SourceOfRandomness random, GenerationStatus status) {
        
        String title = generateString(random, status);
        String xAxisLabel = generateString(random, status);
        String yAxisLabel = generateString(random, status);
        PlotOrientation orientation = random.nextBoolean() ? PlotOrientation.VERTICAL : PlotOrientation.HORIZONTAL;
        boolean legend = random.nextBoolean();
        boolean tooltips = random.nextBoolean();
        boolean urls = random.nextBoolean();
        
        // there are several factory methods with the same signature, original test case uses
        // createXYLineChart -> try a few of them
        
        DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
        DefaultXYDataset xyDataset = new DefaultXYDataset();
        DefaultXYZDataset xyzDataset = new DefaultXYZDataset();
        DefaultIntervalXYDataset intervalXyDataset = new DefaultIntervalXYDataset();
        WaferMapDataset waferDataset = new WaferMapDataset(random.nextInt(0,MAX_CHIPSIZE), random.nextInt(0,MAX_CHIPSIZE));
        int tries = MAX_TRIES;
        while(tries --> 0){
            try{
                switch(random.nextInt(0,10)){ 
                default:
                case 0:
                    return ChartFactory.createXYLineChart(title, xAxisLabel,  yAxisLabel, xyDataset, 
                    orientation, legend, tooltips, urls); 

                case 1:
                    return ChartFactory.createAreaChart(title, xAxisLabel,  yAxisLabel, categoryDataset, 
                    orientation, legend, tooltips, urls); 

                case 2:
                    return ChartFactory.createBarChart(title, xAxisLabel,  yAxisLabel, categoryDataset, 
                    orientation, legend, tooltips, urls); 
                case 3: 
                    return ChartFactory.createBubbleChart(title, xAxisLabel,  yAxisLabel, xyzDataset, 
                    orientation, legend, tooltips, urls); 
                case 4: 
                    return ChartFactory.createHistogram(title, xAxisLabel,  yAxisLabel, intervalXyDataset, 
                    orientation, legend, tooltips, urls); 
                case 5: 
                    return ChartFactory.createLineChart(title, xAxisLabel,  yAxisLabel, categoryDataset, 
                    orientation, legend, tooltips, urls); 
                case 6: 
                    return ChartFactory.createScatterPlot(title, xAxisLabel,  yAxisLabel, xyDataset, 
                    orientation, legend, tooltips, urls); 
                case 7: 
                    return ChartFactory.createStackedAreaChart(title, xAxisLabel,  yAxisLabel, categoryDataset, 
                    orientation, legend, tooltips, urls); 
                case 8: 
                    return ChartFactory.createWaferMapChart(title, waferDataset, 
                    orientation, legend, tooltips, urls); 
                case 9: 
                    return ChartFactory.createXYStepAreaChart(title, xAxisLabel,  yAxisLabel, xyDataset, 
                    orientation, legend, tooltips, urls);         
                case 10: 
                    return ChartFactory.createXYStepChart(title, xAxisLabel,  yAxisLabel, xyDataset, 
                    orientation, legend, tooltips, urls); 
                }
            } catch(Exception e){ // there may be errors already during the creation of charts
                System.err.println("Error in usage simulation, tries left: " + tries);
            }
        }
        return null;
    }
    
}
