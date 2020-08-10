package fieldtest.test;

import junit.framework.TestCase;
import static org.junit.Assume.assumeTrue;

import org.apache.commons.lang3.StringUtils;
import fieldtest.aspect.TestStorage;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

public class NumberUtilsTestField extends TestCase {
	
	private boolean isJavaLong(String number){
        try{ 
            Long.valueOf(number);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
	}
	private boolean isJavaFloat(String number){
        try{ 
            Float.valueOf(number);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
	}
	private boolean isJavaDouble(String number){
        try{ 
            Double.valueOf(number);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
	}
	// based on JavaDoc of NumberUtils.isNumber: "Checks whether the String a valid Java number...
	// ... numbers marked with a type qualifier (e.g. 123L)."
	// so we will check some type qualifiers here 
    @Test
    public void testIsNumber() {
    	String number = TestStorage.number;
    	assumeTrue(number != null);
    	if(number.endsWith("L") || number.endsWith("l")){
            assertEquals(isJavaLong(number), NumberUtils.isNumber(number));
    	} else if (number.endsWith("F") || number.endsWith("f")){
            assertEquals(isJavaFloat(number), NumberUtils.isNumber(number));
    	} else if(number.endsWith("D") || number.endsWith("d")){   
            assertEquals(isJavaDouble(number), NumberUtils.isNumber(number));
    	}
    }

}
