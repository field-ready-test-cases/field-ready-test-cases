package fieldtest.test;

import junit.framework.TestCase;

import org.apache.commons.lang3.RandomStringUtils;
import fieldtest.aspect.TestStorage;
import org.junit.Test;
import static org.junit.Assume.assumeTrue;

public class RandomStringUtilsTestField extends TestCase {
    @Test
    public void testRandomStringUtils() {
        // condition taken from Javadoc
        if(TestStorage.count < 0){ 
            try {
                RandomStringUtils.random(TestStorage.count, 
                TestStorage.start, TestStorage.end, TestStorage.letters, TestStorage.numbers, 
                TestStorage.chars, TestStorage.random);
                fail("Expected IllegalArgumentException");
            } catch (IllegalArgumentException ex) {
                // expected
            }
                
        // condition taken from Javadoc
        } else if (TestStorage.chars != null && TestStorage.chars.length < (TestStorage.end - TestStorage.start) + 1){
            try {
                RandomStringUtils.random(TestStorage.count, 
                TestStorage.start, TestStorage.end, TestStorage.letters, TestStorage.numbers, 
                TestStorage.chars, TestStorage.random);
                fail("Expected ArrayIndexOutOfBoundsException ");
            } catch (ArrayIndexOutOfBoundsException ex) {
                // expected
            }
        }
        // generalized testRandomStringUtils 
        else {
            String result = RandomStringUtils.random(TestStorage.count, 
                TestStorage.start, TestStorage.end, TestStorage.letters, TestStorage.numbers, 
                TestStorage.chars, TestStorage.random);

            assertNotNull(result);
            assertEquals(result.length(), TestStorage.count);
            if(TestStorage.letters && !TestStorage.numbers){
                // based on Javadoc
                for(int i = 0; i < result.length(); i++) 
                    assertEquals("result contains number but should not", true, Character.isLetter(result.charAt(i)) && !Character.isDigit(result.charAt(i)));
            }
            if(!TestStorage.letters && TestStorage.numbers){
                // based on Javadoc
                for(int i = 0; i < result.length(); i++) 
                    assertEquals("result contains letter but should not", true, !Character.isLetter(result.charAt(i)) && Character.isDigit(result.charAt(i)));
            }
        }
    }

}
