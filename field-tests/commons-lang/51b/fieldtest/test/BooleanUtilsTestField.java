package fieldtest.test;

import junit.framework.TestCase;
import static org.junit.Assume.assumeTrue;

import org.apache.commons.lang.BooleanUtils;
import fieldtest.aspect.TestStorage;
import org.junit.Test;
import java.util.List;
import java.util.Arrays;

public class BooleanUtilsTestField extends TestCase {
	
    public static final List<String> TRUE_STRINGS = Arrays.asList(
        new String[]{"on","yes", "true"});

    // based on Javadoc: BooleanUtils.toBoolean "converts a String to a boolean (optimised for 
    // performance). 'true', 'on' or 'yes' (case insensitive) will return true. 
    // Otherwise, false is returned."
    // Note that newer versions also accept "y" and "t" as true.
    @Test
    public void testToBoolean() {
        if(TestStorage.s == null){
    	    assertEquals(false,BooleanUtils.toBoolean(TestStorage.s));
        }   
        else if(TRUE_STRINGS.contains(TestStorage.s.toLowerCase()))
    	    assertEquals(TestStorage.s + " interpreted as false", true,BooleanUtils.toBoolean(TestStorage.s));
        else
    	    assertEquals(false,BooleanUtils.toBoolean(TestStorage.s));
    }    
  

}
