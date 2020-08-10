package fieldtest.test;

import junit.framework.TestCase;
import fieldtest.logging.SingletonFieldTestLogger;
import static org.junit.Assume.assumeTrue;

import fieldtest.aspect.TestStorage;
import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.junit.Test;

public class ExtendedMessageFormatTestField extends TestCase {
	
	// JavaDoc of java.text.MessageFormat says "single quote itself must be represented by doubled 
	// single quotes '' throughout a String" -> we check whether '' is correctly mapped to '
	// another test could check for quoting in general (this is a bit more complex, though)
    @Test
    public void testQuotingOfQuotes() {
    	String result = "";
    	try{
            ExtendedMessageFormat emf = new ExtendedMessageFormat(TestStorage.pattern, TestStorage.registry);
            result = emf.format(new Object[] {"Dummy"});
        } catch(IllegalArgumentException e){
            // we may see an illegal argument exception, e.g., if there is a format string in the 
            // pattern which has not been registered
            // we don't want to test for that in this test cases
            assumeTrue(false);
        }
        SingletonFieldTestLogger.getInstance().info("Result: " + result);
        String pattern = TestStorage.pattern;
		int nrQuotes = 0;
		int i = 0; 
		while(i < pattern.length()-1){
			if(pattern.charAt(i) == '\'' && pattern.charAt(i+1)== '\''){
				nrQuotes++;
				i += 2;
			} else {
				i+=1;
			}
		}
		i = 0;
		int nrQuotesResult = 0;
		while(i < result.length()){
			if(result.charAt(i) == '\''){
				nrQuotesResult++;
            }
            i++;
		}
		assertEquals(nrQuotes, nrQuotesResult);
    }

}
