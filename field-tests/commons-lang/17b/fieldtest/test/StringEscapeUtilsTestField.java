package fieldtest.test;

import junit.framework.TestCase;
import static org.junit.Assume.assumeTrue;
import static org.junit.Assert.*;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import fieldtest.aspect.TestStorage;
import org.junit.Test;

public class StringEscapeUtilsTestField extends TestCase {
	
	// Adapted from StringEscapeUtilsTest.testEscapeXml()
    @Test
    public void testCreateNumber() {
    	String s = TestStorage.s;
        if(!s.contains(">") && !s.contains("<") && !s.contains("&") && 
                   !s.contains("\"") && !s.contains("'"))
            assertEquals(s, StringEscapeUtils.escapeXml(s));
        else{
            // for some reason when I run test cases via reflection, they are run as JUnit 3 test cases
            // so we don't have assertNotEquals
            assertFalse(s.equals(StringEscapeUtils.escapeXml(s)));
        }
    }

}
