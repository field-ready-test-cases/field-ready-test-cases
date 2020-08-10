package fieldtest.test;

import junit.framework.TestCase;

import org.apache.commons.lang3.StringUtils;
import fieldtest.aspect.TestStorage;
import org.junit.Test;

import static org.junit.Assume.assumeTrue;

public class StringUtilsTestField extends TestCase {
	// Generalization of examples in JavaDoc
    @Test
    public void testReplaceEach() {
        String text = TestStorage.text;
        String[] searchList = TestStorage.searchList;
        String[] replacementList = TestStorage.replacementList;
        if(text == null){
            assertNull(StringUtils.replaceEach(text, searchList, replacementList));
        } else if (searchList == null || replacementList == null) {
            assertEquals(text, StringUtils.replaceEach(text, searchList, replacementList));
        } else if (searchList.length == 1 && replacementList.length == 1){
            // example specifies that null means that nothing should happen            
            if(searchList[0] == null || replacementList[0] == null){
                assertEquals(text,StringUtils.replaceEach(text, searchList, replacementList));
            }
            else if (!searchList[0].isEmpty()){
                // examples do not specify behavior for empty search strings
                // but for other cases, we test against JDK 
                String stdlibReplace = text.replace(searchList[0], replacementList[0]);
                assertEquals(stdlibReplace,StringUtils.replaceEach(text, searchList, replacementList));
            } else { // in general, the result should not be null, unless text is null
                assertNotNull(StringUtils.replaceEach(text, searchList, replacementList));
            }
        } else { // otherwise the full behavior cannot be inferred from the examples
            assertNotNull(StringUtils.replaceEach(text, searchList, replacementList));
        }
    	try {
    		StringUtils.replaceEach(text, searchList, replacementList);
    	} catch(IllegalArgumentException  e) {
            // only case where IllegalArgumentException is allowed is when 
            // searchList.length != replacementList.length
            // Javadoc: "if the lengths of the arrays are not the same (null is ok, and/or size 0)"
            assertTrue("seach list and replacement list have different length", 
                searchList.length != replacementList.length);
        }
    }

}
