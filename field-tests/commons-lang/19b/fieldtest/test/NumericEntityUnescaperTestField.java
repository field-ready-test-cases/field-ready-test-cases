package fieldtest.test;

import junit.framework.TestCase;

import static org.junit.Assume.assumeNotNull;
import static org.junit.Assume.assumeTrue;

import fieldtest.aspect.TestStorage;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

public class NumericEntityUnescaperTestField extends TestCase {
	
	// Adapted from NumericEntityUnescaperTest.testOutOfBounds() and 
    // and testUnfinishedEntity()
    // original tests check whether translating unfinished entities produces
    // some result 
    @Test
    public void testSupplementaryUnescaping() {
    	String s = TestStorage.s;
        NumericEntityUnescaper neu = new NumericEntityUnescaper();
    	assumeNotNull(s);
        // assume that the string does not contain a finished entity
        // for some reason when I run test cases via reflection, they are run with a runner that does
        // not support assumeFalse
        assumeTrue(!s.matches(".*&#[xX]?[\\dA-Fa-f]+;.*")); 
    	assertNotNull(neu.translate(s));
    	
    }

}
