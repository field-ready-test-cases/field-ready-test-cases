package fieldtest.logging;

import java.io.IOException;
import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
/**
 * A singleton holding a logger for logging information during field-testing 
 * experiments. The log-file location can be specified via the system properties
 * "log-directory" and "field-test-id", where the former specifies the directory
 * and latter specifies of the form "test_'field-test-id'.log".
 *
 */
public class SingletonFieldTestLogger {

    private static Logger instance;
    
    public static void reset() {
    	synchronized (Logger.class) {
			instance = null;
		}
    }

    public static Logger getInstance() {
        synchronized (Logger.class) {
            if (instance == null) {
                Logger logger = Logger.getLogger("FieldTestLogger");
                FileHandler fh;

                try {
                    String fieldTestId = System.getProperty("field-test-id");
                    String logDirectory = System.getProperty("log-directory");
                    if(fieldTestId == null || logDirectory == null){
                        logger.warning("Cannot log to file");   
                    }
                    else{
                        // This block configure the logger with handler and formatter
                        new File(logDirectory).mkdirs();
                        fh = new FileHandler(logDirectory + "/test_" + 
                             fieldTestId + ".log");
                        logger.addHandler(fh);
                        SimpleFormatter formatter = new SimpleFormatter();
                        fh.setFormatter(formatter);
                        instance = logger;
                    }

                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return instance;
    }

}