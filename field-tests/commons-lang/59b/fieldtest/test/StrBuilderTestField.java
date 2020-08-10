package fieldtest.test;

import junit.framework.TestCase;
import static org.junit.Assume.assumeTrue;

import org.apache.commons.lang.text.StrBuilder;
import fieldtest.aspect.TestStorage;
import org.junit.Test;

import fieldtest.logging.SingletonFieldTestLogger;

public class StrBuilderTestField extends TestCase {
    // the field test targets the case of appendFixedWidthPadRight, where the width is lower 
    // than the appended object's length
	// Based on Javadoc: If the object is larger than the length, the right hand side is lost
    @Test
    public void testAppendRight() {
    	StrBuilder sb = new StrBuilder(TestStorage.sb.toString()); // copy argument
        assumeTrue(TestStorage.obj != null && TestStorage.width < TestStorage.obj.toString().length());
        String beforeAppend = sb.toString();
        SingletonFieldTestLogger.getInstance().info(beforeAppend + ":" + TestStorage.obj + " with " + TestStorage.width);
        
        sb.appendFixedWidthPadRight(TestStorage.obj, TestStorage.width, TestStorage.padChar);
        String padding = TestStorage.obj.toString().substring(0,TestStorage.width);
        
        SingletonFieldTestLogger.getInstance().info("After append " + sb.toString());
        assertEquals(beforeAppend + padding, sb.toString());
    }

}
