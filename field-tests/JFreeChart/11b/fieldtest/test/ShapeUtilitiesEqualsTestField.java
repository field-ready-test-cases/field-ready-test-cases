package fieldtest.test;

import java.awt.geom.GeneralPath;

import org.jfree.chart.util.ShapeUtilities;
import fieldtest.aspect.TestStorage;

import junit.framework.TestCase;

public class ShapeUtilitiesEqualsTestField extends TestCase {
	
	// Test ShapeUtilities.equals methods, generalizing from the in-house test case ShapeUtilitiesTests.testEqualShapes
	// Note that the original test case only checks the positive cases, which we use here assumption, 
	// since it is covered by the in-house tests. 
	// For the field test, we focus on the negative case:
	// we check whether manipulating one of two equal general paths changes the outcome 
	// of ShapeUtilities.equal. This should be the case.
	
    public void testEqualGeneralPaths() {
    
        GeneralPath p2 = TestStorage.p2;
        org.junit.Assume.assumeTrue(TestStorage.p1 instanceof GeneralPath);
        org.junit.Assume.assumeTrue(ShapeUtilities.equal(TestStorage.p1, p2));
        
        GeneralPath p1_1 = (GeneralPath) TestStorage.p1.clone();
        GeneralPath p1_2 = (GeneralPath) TestStorage.p1.clone();
        GeneralPath p1_3 = (GeneralPath) TestStorage.p1.clone();
        GeneralPath p1_4 = (GeneralPath) TestStorage.p1.clone();
    
        org.junit.Assume.assumeTrue(ShapeUtilities.equal(p1_1, p2));
        
        p1_1.lineTo(1.0f, 2.0f);
        assertFalse(ShapeUtilities.equal(p1_1, p2));
        p1_2.curveTo(5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f);
        assertFalse(ShapeUtilities.equal(p1_2, p2));
        p1_3.moveTo(1.0f, 2.0f);
        assertFalse(ShapeUtilities.equal(p1_3, p2));
        p1_4.quadTo(1.0f, 2.0f, 3.0f, 4.0f);
        assertFalse(ShapeUtilities.equal(p1_4, p2));
    }
        
        
}
