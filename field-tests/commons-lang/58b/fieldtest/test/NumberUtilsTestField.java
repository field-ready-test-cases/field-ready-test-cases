package fieldtest.test;

import junit.framework.TestCase;
import static org.junit.Assume.assumeTrue;

import fieldtest.aspect.TestStorage;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;
import java.math.BigInteger;

public class NumberUtilsTestField extends TestCase {
	
	// Based on Javadoc of NumberUtils.createNumber (check if string can be converted if it contains a number)
	// check against JDK and consider integral numbers for this test case (there is another test case
	// for decimal numbers
	
	private boolean isJavaInteger(String number){
        try{
            Integer.valueOf(number);
            return true;
        } catch(NumberFormatException e1){
            try{
                String longNumber = number;
                // suffixes are fine for createNumber but not for Long.valueOf
                // which is actually not consistent with Double.valueOf
                if(number.endsWith("l") || number.endsWith("L"))
                    longNumber = number.substring(0,number.length() -1);
                Long.valueOf(longNumber);
                return true;
            } catch(NumberFormatException e2){
                try{
                    new BigInteger(number);
                    return true;
                } catch(NumberFormatException e3){
                    return false;
                }
            }
        }
	}
	
    @Test
    public void testCreateNumber() {
    	String number = TestStorage.number;
    	assumeTrue(isJavaInteger(number));
        try{
            NumberUtils.createNumber(number);            
        } catch (NumberFormatException e){
            fail("Could not create number from " + number);
        }
    }

}
