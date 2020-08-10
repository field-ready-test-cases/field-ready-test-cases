package fieldtest.usage;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.generator.java.lang.AbstractStringGenerator;

import org.jfree.chart.renderer.category.*; // simply import all category renderers 
import org.jfree.chart.plot.CategoryPlot;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultIntervalCategoryDataset;
import org.jfree.data.general.DefaultKeyedValues2DDataset;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;
import org.jfree.data.gantt.TaskSeriesCollection;

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;

import org.jfree.chart.renderer.category.StatisticalBarRenderer;


public class SimulateUsageGenerator extends Generator<CategoryPlot> {

    private static final double PROBABILITY_ADD_DATA = 0.7;
    public static final int MAX_NR_ELEMS = 4;
    public SimulateUsageGenerator() {
        super(CategoryPlot.class);
    }
    
    // copied from generator for Bug 12
    public CategoryDataset generateDataset(SourceOfRandomness random){
        CategoryDataset dataset = null;
        switch(random.nextInt(6)){
                case 0:
                    dataset = null;
                    break;
                case 1:
                    dataset = new DefaultCategoryDataset();
                    if(random.nextBoolean()) // add a value
                        ((DefaultCategoryDataset)dataset).addValue((double)random.nextDouble(), 
                                                    (Character)random.nextChar('a','z'),
                                                    (Character)random.nextChar('a','z'));
                    break;
                case 2:
                    int nrSeries = random.nextInt(0,MAX_NR_ELEMS);
                    int nrCategories = random.nextInt(0,MAX_NR_ELEMS);
                    double[][] starts = null;
                    double[][] ends = null;
                    if(nrSeries == 0 || nrCategories == 0){
                        starts = new double[0][0];
                        ends = new double[0][0];
                    } else{
                        starts = new double[nrSeries][nrCategories];
                        ends = new double[nrSeries][nrCategories];
                        for(int i = 0; i < nrSeries;i++){
                            for(int j = 0; j < nrCategories; j++){
                                starts[i][j] = random.nextDouble();
                                ends[i][j] = random.nextDouble();
                            }
                        }
                    }
                    dataset = new DefaultIntervalCategoryDataset(starts,ends);
                    break;
                case 3:
                    dataset = new DefaultKeyedValues2DDataset(); // subclass of DefaultCategoryDataset
                    if(random.nextBoolean()) // add a value
                        ((DefaultCategoryDataset)dataset).addValue((double)random.nextDouble(), 
                                                    (Character)random.nextChar('a','z'),
                                                    (Character)random.nextChar('a','z'));
                    break;
                case 4:
                    dataset = new DefaultStatisticalCategoryDataset();
                    int nrElems = random.nextInt(0, MAX_NR_ELEMS);
                    for(int i = 0; i < nrElems; i ++){
                        if(random.nextBoolean()){
                        // add a mean and std dev
                            ((DefaultStatisticalCategoryDataset)dataset).add((double)random.nextDouble(), 
                                                    (double)random.nextDouble(),
                                                    (Character)random.nextChar('a','z'),
                                                    (Character)random.nextChar('a','z'));                       
                        } else {
                        // try null as mean 
                            ((DefaultStatisticalCategoryDataset)dataset).add(null, 
                                                    new Double(random.nextDouble()),
                                                    (Character)random.nextChar('a','z'),
                                                    (Character)random.nextChar('a','z'));
                        
                        }
                
                    }
                case 5:
                default:
                    dataset = new TaskSeriesCollection();
            }
        return dataset;
    }
    
    @Override
    public CategoryPlot generate(SourceOfRandomness random, GenerationStatus status) {
        AbstractCategoryItemRenderer renderer = null;
        switch(random.nextInt(8)) { // choose one of eight subclasses
            case 0: 
                renderer = new AreaRenderer();
            break;
            case 1: 
                renderer = new BarRenderer();
            break;
            case 2: 
                renderer = new BoxAndWhiskerRenderer();
            break;
            case 3: 
                renderer = new CategoryStepRenderer();
            break;
            case 4: 
                renderer = new LevelRenderer();
            break;
            case 5: 
                renderer = new LineAndShapeRenderer(); // used in the original test case
            break;
            case 6:
                renderer = new StatisticalBarRenderer();            
            case 7:
            default:
                renderer = new MinMaxCategoryRenderer();
            break;
        }
        
        CategoryDataset dataset = generateDataset(random); 
        CategoryPlot plot = new CategoryPlot();
        plot.setRenderer(renderer);
        plot.setDataset(dataset);
        
        return plot; 
    }
}
