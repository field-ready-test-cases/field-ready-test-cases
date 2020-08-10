package fieldtest.test;

import junit.framework.TestCase;
import static org.junit.Assume.assumeTrue;

import org.apache.commons.lang3.StringUtils;
import fieldtest.aspect.TestStorage;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;
import java.math.BigDecimal;

public class NumberUtilsTestField extends TestCase {
	
	// Based on Javadoc which says "Checks whether the String a valid Java number", we consider here
	// decimal numbers, i.e., floats, doubles or BigDecimals
	// informally we check: "number is float" or "number is double" or "number is BigDecimal" 
	// implies NumberUtils.isNumber(number)
	
	private boolean isJavaDecimal(String number){
        try{
            Float.valueOf(number);
            return true;        
        } catch(NumberFormatException e1){
            try{
                Double.valueOf(number);
                return true;        
            } catch(NumberFormatException e2){
                try{
                    new BigDecimal(number);
                    return true;
                } catch(NumberFormatException e3){
                    return false;
                }
            }       
        }
	}
	
    @Test
    public void testIsNumber() {
        String number = TestStorage.number;
    	// is java decimal -> isNumber
        assertTrue(number + " is a valid decimal, but not recognized as one.",
            !isJavaDecimal(number) || NumberUtils.isNumber(number));
    }

}
