package fieldtest.test;

import junit.framework.TestCase;

import org.apache.commons.lang3.StringUtils;
import fieldtest.aspect.TestStorage;
import org.junit.Test;

public class StringUtilsTestField extends TestCase {
	
	// based on JavaDoc: StringUtils.equals "compares two CharSequences, returning true if they represent equal sequences of characters."
	// two null values are considered equal
    @Test
    public void testStringUtilsEquals() {
    	CharSequence cs1 = TestStorage.cs1;
    	CharSequence cs2 = TestStorage.cs2;
    	boolean equal = false;
    	if(cs1 == null){
            equal = cs2 == null;
    	} else if (cs1 != null && cs2 != null && cs1.length() == cs2.length()){
            equal = true;
            for(int i = 0; i < cs1.length(); i++){
                if(cs1.charAt(i) != cs2.charAt(i)){
                    equal = false;
                    break;
                }
            }            
    	}
    	assertEquals(equal,StringUtils.equals(cs1, cs2));
    }

}
