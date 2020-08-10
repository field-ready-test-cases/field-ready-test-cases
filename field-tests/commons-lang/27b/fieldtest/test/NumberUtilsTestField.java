package fieldtest.test;

import junit.framework.TestCase;
import static org.junit.Assume.assumeTrue;

import org.apache.commons.lang3.StringUtils;
import fieldtest.aspect.TestStorage;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

public class NumberUtilsTestField extends TestCase {
	
	// based on JavaDoc which says that an exception shall be thrown if the string cannot be converted
	// -> check that using NumberUtils.isNumber
    @Test
    public void testCreateNumber() {
    	String number = TestStorage.number;
        assumeTrue(!NumberUtils.isNumber(number)); // .endsWith("eE"));
        boolean success = false;
         try {
            Object obj = NumberUtils.createNumber(number);
        } catch (NumberFormatException e) {
            success = true;
        }
        assertTrue(success);
    }

}
