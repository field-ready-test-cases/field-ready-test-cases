package fieldtest.test;

import junit.framework.TestCase;
import static org.junit.Assume.assumeTrue;
import static org.junit.Assume.assumeNoException;

import org.apache.commons.lang3.StringUtils;
import fieldtest.aspect.TestStorage;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

public class NumberUtilsTestField extends TestCase {
	
	// Adapted from testCreateNumber()
    @Test
    public void testCreateNumber() {
        String number = TestStorage.number;
        if(!NumberUtils.isNumber(number)){
            try {
                NumberUtils.createNumber(number);
                fail("Expected NumberFormatException for " + number);
            } catch (NumberFormatException nfe) {
                // expected
            }
        }
    }
}
