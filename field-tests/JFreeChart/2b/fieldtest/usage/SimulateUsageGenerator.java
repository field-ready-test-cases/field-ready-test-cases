package fieldtest.usage;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.generator.java.lang.AbstractStringGenerator;

import org.jfree.chart.renderer.category.*; // simply import all category renderers 
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYIntervalSeriesCollection;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYIntervalSeries;

public class SimulateUsageGenerator extends Generator<XYDataset> {

    private static final double PROBABILITY_ADD_DATA = 0.7;
    private static final int MAX_NR_SERIES = 3;
    private static final int MAX_NR_DATAPOINTS = 5;
    private static final double NAN_PROBABILITY = 0.025;
    private static final double INFTY_PROBABILITY = 0.025;
    
    public SimulateUsageGenerator() {
        super(XYDataset.class);
    }

    public double generateDoubleValue(SourceOfRandomness random){
        if(random.nextDouble() < NAN_PROBABILITY){
            return Double.NaN;
        } else if(random.nextDouble() < INFTY_PROBABILITY){
            // so actually, the infty probability is lower than INFTY_PROBABILITY
            return random.nextBoolean() ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY; 
        } else{
            return random.nextDouble() * 10;
        }
    }
    @Override
    public XYDataset generate(SourceOfRandomness random, GenerationStatus status) {
        
        XYIntervalSeriesCollection d = new XYIntervalSeriesCollection();
        int nrSeries = random.nextInt(1,MAX_NR_SERIES);
        for(int iSeries = 0; iSeries < nrSeries; iSeries ++){
            XYIntervalSeries s = new XYIntervalSeries("S" + iSeries);
            int nrDataPoints = random.nextInt(1,MAX_NR_DATAPOINTS);
            for(int iData = 0; iData < nrDataPoints; iData ++){
                double x = generateDoubleValue(random); 
                double xInterval1 = generateDoubleValue(random);
                double xInterval2 = generateDoubleValue(random);
                double y = generateDoubleValue(random);
                double yInterval1 = generateDoubleValue(random);
                double yInterval2 = generateDoubleValue(random);

                s.add(x, xInterval1,xInterval2, y, yInterval1,yInterval2);
            }
            d.addSeries(s);
        }
        return d; 
    }
}
