package fieldtest.test;

import junit.framework.TestCase;
import static org.junit.Assume.assumeTrue;

import org.apache.commons.lang3.StringUtils;
import fieldtest.aspect.TestStorage;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

public class NumberUtilsTestField extends TestCase {
	
	// Adapted from NumberUtilsTest.testCreateNumber() and information taken from JavaDoc of 
	// NumberUtils.createNumber
    @Test
    public void testCreateNumber() {
    	String number = TestStorage.number;
        // from JavaDoc: If the string starts with 0x or -0x 
        if(number.startsWith("0x") || number.startsWith("-0x") || 
           number.startsWith("0X") || number.startsWith("-0X")){
          assumeTrue(number.startsWith("-") ? number.length() > 3 : number.length() > 2);
          String subNumber = number.substring(number.startsWith("-") ? 3 : 2);
          for (char c : subNumber.toCharArray()){
            // check if each character is a hex decimal digit
            assumeTrue(Character.digit(c,16) != -1);
          }
        }
        else // could be further relaxed
        	assumeTrue(StringUtils.isNumeric(number));
        assertNotNull(NumberUtils.createNumber(number));
    }

}
