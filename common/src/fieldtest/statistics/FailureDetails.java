package fieldtest.statistics;

/**
 * A class for storing information about a field test failure, i.e., information
 * about the assertion that caused the corresponding parameterized unit test 
 * case to fail.
 * 
 */
public class FailureDetails {
	
	private String description;
	private String message;
	private String exceptionClassName;
	private String trace;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getExceptionClassName() {
		return exceptionClassName;
	}
	public void setExceptionClassName(String exceptionClassName) {
		this.exceptionClassName = exceptionClassName;
	}
	public String getTrace() {
		return trace;
	}
	public void setTrace(String trace) {
		this.trace = trace;
	}
	public FailureDetails() {
		
	}
	public FailureDetails(String description, String message, String exceptionClassName, String trace) {
		super();
		this.description = description;
		this.message = message;
		this.exceptionClassName = exceptionClassName;
		this.trace = trace;
	}
	@Override
	public String toString() {
		return "FailureDetails [description=" + description + ", message=" + message + ", exceptionClassName="
				+ exceptionClassName + ", trace=" + trace + "]";
	}
	
	
}
