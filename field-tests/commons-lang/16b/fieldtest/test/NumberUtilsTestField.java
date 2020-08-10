package fieldtest.test;

import junit.framework.TestCase;
import static org.junit.Assume.assumeTrue;

import org.apache.commons.lang3.StringUtils;
import fieldtest.aspect.TestStorage;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

public class NumberUtilsTestField extends TestCase {
	
	// Adapted from NumberUtilsTest.testCreateNumber()
    @Test
    public void testCreateNumber() {
    	String number = TestStorage.number;
        if(number.startsWith("0x") || number.startsWith("0X")){
          assumeTrue(number.length() > 2);
          for (char c : number.substring(2).toCharArray()){
            // check if each character is a hex decimal digit
            assumeTrue(Character.digit(c,16) != -1);
          }
        }
        else
        	assumeTrue(StringUtils.isNumeric(number));
        assertNotNull(NumberUtils.createNumber(number));
    }

}
