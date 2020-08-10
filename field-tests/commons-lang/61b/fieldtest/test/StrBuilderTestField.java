package fieldtest.test;

import junit.framework.TestCase;
import static org.junit.Assume.assumeTrue;

import org.apache.commons.lang.text.StrBuilder;
import fieldtest.aspect.TestStorage;
import org.junit.Test;

public class StrBuilderTestField extends TestCase {
	
	// Adapted from StrBuilderTest.testDeleteAll_String()
    @Test
    public void testDeleteAll_String() {
        StrBuilder sb = TestStorage.sb;
        String toDelete = TestStorage.s;
        // generally test against JDK functions
        if(toDelete == null || toDelete.equals("") || 
            !sb.toString().contains(toDelete)){ 
            String beforeDeletion = sb.toString();
            sb.deleteAll(TestStorage.s);
            assertEquals(beforeDeletion, sb.toString());
        } else {
            String beforeDeletion = sb.toString();
            String reference = beforeDeletion.replace(toDelete, "");
            sb.deleteAll(TestStorage.s);
            assertEquals(reference, sb.toString());
        }
    }

}
