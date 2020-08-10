package fieldtest.test;

import java.awt.geom.Line2D;

import org.jfree.chart.util.ShapeList;
import fieldtest.aspect.TestStorage;

import junit.framework.TestCase;

public class ShapeListTestField extends TestCase {
	
	// test whether equal and clone work according to their contracts, check reflexivity and check
	// whether adding the same value twice works
	// add same value as in testCloning()	

	public void testEquals() {
        ShapeList lField = TestStorage.shapeList;
        
        try{
            ShapeList l1 = (ShapeList)lField.clone();
            ShapeList l2 = (ShapeList)lField.clone();
            int nrItems = l1.size();
            assertTrue(l1.equals(l2));
            assertTrue(l2.equals(l1));
            
            l1.setShape(nrItems, new Line2D.Double(1.0, 2.0, 3.0, 4.0));
            l2.setShape(nrItems, new Line2D.Double(1.0, 2.0, 3.0, 4.0));
            assertTrue(l1.equals(l2));
            assertTrue(l2.equals(l1));
            
        } catch(CloneNotSupportedException e){
            fieldtest.logging.SingletonFieldTestLogger.getInstance()
            .info("Could not execute test case, because cloning is not supported");
        }
    }

}
