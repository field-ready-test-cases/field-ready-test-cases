package fieldtest.usage;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.generator.java.lang.AbstractStringGenerator;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.CubicCurve2D;

import org.jfree.chart.util.ShapeList;

public class SimulateUsageGenerator extends Generator<ShapeList> {

    private static final int MAX_NR_ELEMS = 5;
    private static final int MAX_VALUE = 10;
    public SimulateUsageGenerator() {
        super(ShapeList.class);
    }
    
    public int generateInt(SourceOfRandomness random){
        return random.nextInt(-MAX_VALUE, MAX_VALUE);
    }
    public double generateDouble(SourceOfRandomness random){
        return random.nextDouble()*2 * MAX_VALUE - MAX_VALUE;
    }
    public Shape generateShape(SourceOfRandomness random) {
        switch(random.nextInt(0,6)){  // generate a few different shape
            case 0:
                return new Rectangle(generateInt(random), generateInt(random),
                        generateInt(random), generateInt(random));
            case 1:  
                return new Line2D.Double(generateDouble(random), generateDouble(random),
                        generateDouble(random), generateDouble(random));
            case 2:
                
                return new Arc2D.Double(generateDouble(random), generateDouble(random),
                        generateDouble(random), generateDouble(random), generateDouble(random), 
                        generateDouble(random), Arc2D.OPEN);
            case 3:
                return new Ellipse2D.Double(generateDouble(random), generateDouble(random),
                        generateDouble(random), generateDouble(random));
            case 4:
                return new RoundRectangle2D.Double(generateDouble(random), generateDouble(random),
                        generateDouble(random), generateDouble(random),generateDouble(random),generateDouble(random));
            case 5:
                return new CubicCurve2D.Double(generateDouble(random), generateDouble(random),
                        generateDouble(random), generateDouble(random),generateDouble(random),generateDouble(random),
                        generateDouble(random),generateDouble(random));
            default:
                return null;        
        }
    }
    
    @Override
    public ShapeList generate(SourceOfRandomness random, GenerationStatus status) {
        ShapeList s = new ShapeList();
        int nrElems = random.nextInt(0, MAX_NR_ELEMS);
        for(int i = 0; i < nrElems; i++){
            s.setShape(i, generateShape(random));
        }
        
        return s; 
    }
}
