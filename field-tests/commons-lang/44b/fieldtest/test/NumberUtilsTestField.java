package fieldtest.test;

import junit.framework.TestCase;
import static org.junit.Assume.assumeTrue;

import fieldtest.aspect.TestStorage;
import org.apache.commons.lang.NumberUtils;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

public class NumberUtilsTestField extends TestCase {
    // check if number format exceptions are thrown for string not containing numbers
    @Test
    public void testCreateNumberExceptions() {
    	String number = TestStorage.number;
    	boolean isNumber = NumberUtils.isNumber(number);
        boolean exceptionThrown = false;
         try {
            Object obj = NumberUtils.createNumber(number);
        } catch (NumberFormatException e) {
            exceptionThrown = true;
        }
        // check (! isNumber) -> exceptionThrown
        assertTrue(isNumber || exceptionThrown);
    }

}
