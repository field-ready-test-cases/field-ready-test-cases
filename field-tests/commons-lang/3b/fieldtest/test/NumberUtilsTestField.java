package fieldtest.test;

import junit.framework.TestCase;
import static org.junit.Assume.assumeTrue;
import static org.junit.Assume.assumeNoException;

import org.apache.commons.lang3.StringUtils;
import fieldtest.aspect.TestStorage;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

public class NumberUtilsTestField extends TestCase {
	
	// Adapted from testStringCreateNumber() and from JavaDoc which reads
	// "If a type specifier is not found, it will check for a decimal point and then try 
	// successively larger types from Integer to BigInteger and from Float to BigDecimal"
	// -> we check whether appropriately large types are created 
    @Test
    public void testCreateNumber() {
        String number = TestStorage.number;
        // only allow well-formed double strings -> check against JDK implementation
        try {
            double jdkParsedDouble = Double.valueOf(number);
            // check only for non-zero, parsed double may be zero, but it could 
            // actually be a rather small number which fits into BigDecimal
            if(jdkParsedDouble != 0){
                if ((jdkParsedDouble > 0 && jdkParsedDouble <= Float.MAX_VALUE
                    && jdkParsedDouble >= Float.MIN_VALUE) || 
                    (jdkParsedDouble < 0 && jdkParsedDouble >= -Float.MAX_VALUE
                    && jdkParsedDouble <= -Float.MIN_VALUE) ){
	                assertTrue(NumberUtils.createNumber(number) instanceof Float);
                }
                // maybe NaN or infinity occur
                else if ((jdkParsedDouble > 0 && jdkParsedDouble <= Double.MAX_VALUE
                    && jdkParsedDouble >= Double.MIN_VALUE) || 
                    (jdkParsedDouble < 0 && jdkParsedDouble >= -Double.MAX_VALUE
                    && jdkParsedDouble <= -Double.MIN_VALUE))
	                assertTrue(NumberUtils.createNumber(number) instanceof Double);
           }
        } catch (NumberFormatException e) {
            assumeNoException(e);
        }
    }

}
