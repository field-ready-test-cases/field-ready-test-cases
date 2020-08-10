package fieldtest.test;

import junit.framework.TestCase;

import static org.junit.Assume.assumeTrue;

import org.apache.commons.lang3.StringUtils;
import fieldtest.aspect.TestStorage;
import org.junit.Test;
import fieldtest.logging.SingletonFieldTestLogger;
import java.util.logging.Logger;

public class StringUtilsTestField extends TestCase {
	
    private String assertStartAndReplace(String result, String start){
        assertTrue(result.startsWith(start));
        return result.replaceFirst(start, "");
    }
                    

	// Adapted from StringUtilsTest.testJoin_Objects() and testJoin_ArrayChar()
    @Test
    public void testJoin_Objects() {
        assumeTrue(TestStorage.array == null || TestStorage.array.length > 0);
        assumeTrue(TestStorage.startIndex >= 0);
        assumeTrue(TestStorage.array == null || TestStorage.endIndex <= TestStorage.array.length);

    	String result = StringUtils.join(TestStorage.array,
           TestStorage.separator, TestStorage.startIndex, TestStorage.endIndex);
        Logger logger = SingletonFieldTestLogger.getInstance();
        logger.info("Result of join: " + result);

        // do not know how to handle empty arrays from testJoin_ArrayChar()
    	if(TestStorage.array == null)
            assertEquals(null,result);
        else if (TestStorage.startIndex >= TestStorage.endIndex){
            assertEquals("",result);
        }
    	else for (int i = TestStorage.startIndex; i < TestStorage.endIndex; i++){
            Object elem = TestStorage.array[i];
            // behavior inferred from unit test
            // if elem is null then string contains nothing 
            String expected = null;
            if(elem == null) 
                expected = ""; 
            else if(elem.toString() == null)
                expected = "null";
            else 
                expected = elem.toString();
            if(i < TestStorage.endIndex - 1){ 
                // if we are not at the last elem we shall see the delimiter   
                result = assertStartAndReplace(result, expected + TestStorage.separator);
            } else 
                assertTrue(result.equals(expected));
            
        }
    }

}
