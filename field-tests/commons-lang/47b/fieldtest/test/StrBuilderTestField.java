package fieldtest.test;

import junit.framework.TestCase;
import static org.junit.Assume.assumeTrue;

import org.apache.commons.lang.text.StrBuilder;
import fieldtest.aspect.TestStorage;
import org.junit.Test;

public class StrBuilderTestField extends TestCase {
	
	// based on Javadoc of org.apache.commons.lang.text.StrBuilder: 
	// appendFixedWidthPadRight "appends an object to the builder padding 
	// on the right to a fixed length. The toString of the object is used. 
    // If the object is larger than the length, the right hand side is lost. If the object is null, 
    // null text value is used."
    @Test
    public void testAppendRight() {
        assumeTrue(TestStorage.sb != null);
        StrBuilder sb = new StrBuilder(TestStorage.sb.toString()); // copy argument
        String beforeAppend = sb.toString();
        sb.appendFixedWidthPadRight(TestStorage.obj, TestStorage.width, TestStorage.padChar);
        String appendText = TestStorage.obj == null ? "" : TestStorage.obj.toString(); // null text is usually ""
        if(TestStorage.width < appendText.length())
            appendText = appendText.substring(0, TestStorage.width);
        String padding = "";
        for(int i = appendText.length(); i < TestStorage.width; i++)
            padding += TestStorage.padChar;
        assertEquals(beforeAppend + appendText + padding, sb.toString());
    }

}
