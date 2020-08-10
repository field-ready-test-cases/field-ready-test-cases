package fieldtest.triggering.static_anomaly_detection;

import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fieldtest.triggering.parameter_tree.ImmutableList;
import fieldtest.triggering.parameter_tree.TestParameterTreeTraverser;

/**
 * The default implementation of the search for suspicious values in the field
 * test data. This is used by static anomaly detection trigger. 
 * 
 * While it implements basic functionality and initial set of checks, this class 
 * can be subclassed to extend the range of considered suspicious values. 
 */
public class DefaultStaticDetectionTreeTraverser 
											extends TestParameterTreeTraverser{

	// last element in the list is a textual representation of the covered 
	// element
	protected Map<List<String>,Set<String>> coveredPathToElement = new HashMap<>();
	private boolean allowMultipleCoverage = false;

	public DefaultStaticDetectionTreeTraverser(int maxDepth, 
									boolean exploreArrays, boolean buildTree) {
		super(maxDepth, exploreArrays, buildTree);
	}
	public DefaultStaticDetectionTreeTraverser(int maxDepth, 
			boolean exploreArrays, boolean buildTree, boolean allowMultipleCoverage) {
		this(maxDepth,exploreArrays, buildTree);
		this.allowMultipleCoverage = allowMultipleCoverage;
	}
	// denotes whether we found a new "suspicious" value while traversing
	private boolean foundSuspiciousValue = false;
	private Map.Entry<List<String>, String> lastFoundValue = null;

	/**
	 * Check if element has been covered before and return true if not. This
	 * way we only try to find new positions of "suspicious" values. By setting 
	 * <code>allowMultipleCoverage</code> to false, we may disable that.
	 * @param pathIntree
	 * @param coveredSuspiciousValue
	 * @return
	 */
	protected boolean checkCoverage(ImmutableList<String> pathIntree, 
			String coveredSuspiciousValue) {

		List<String> coveredPath = pathIntree.toReversedList();
		Set<String> coveredElems = coveredPathToElement.get(coveredPath);
		if(allowMultipleCoverage) {
			foundSuspiciousValue = true;
			setLastFoundValue(new AbstractMap
					.SimpleImmutableEntry<List<String>, String>(coveredPath, coveredSuspiciousValue));
			return true;
		}
		if(coveredElems != null) {
			if(coveredElems.contains(coveredSuspiciousValue)) {
				return false;
			} else {
				coveredElems.add(coveredSuspiciousValue);
				foundSuspiciousValue = true;
				setLastFoundValue(new AbstractMap
						.SimpleImmutableEntry<List<String>, String>(coveredPath, coveredSuspiciousValue));
				return true;
			}
		} else {				
			coveredElems = new HashSet<String>();
			coveredElems.add(coveredSuspiciousValue);
			coveredPathToElement.put(coveredPath, coveredElems);
			foundSuspiciousValue = true;
			setLastFoundValue(new AbstractMap
					.SimpleImmutableEntry<List<String>, String>(coveredPath, coveredSuspiciousValue));
			return true;
		}
	}
	@Override
	public boolean checkComplexObjectForStopping(Object o, Class<?> t, ImmutableList<String> pathIntree) {
		// just do some hard-coded checks
		if(o == null) {
			if(checkCoverage(pathIntree, "null"))
				return true;
		}

		return false;
	}

	@Override
	public boolean checkStringForStopping(CharSequence string, Class<?> t, ImmutableList<String> pathIntree) {
		if(string == null) {
			if(checkCoverage(pathIntree, "null"))
				return true;
		} else {
			// toString() simply returns the contained string in the CharSequence
			// according to the Javadocs, so we check for the empty string
			if(string.toString().isEmpty()) {
				if(checkCoverage(pathIntree, "empty"))
					return true;
			}
		}
		return false;
	}

	@Override
	public boolean checkArrayForStopping(Object o, Class<?> t, ImmutableList<String> pathIntree) {
		if(o == null) {
			if(checkCoverage(pathIntree, "null"))
				return true;
		} else if(Array.getLength(o) == 0){
			if(checkCoverage(pathIntree, "len-0"))
				return true;
		}
		return false;
	}

	@Override
	public boolean checkPrimitiveValueForStopping(Object o, Class<?> t, ImmutableList<String> pathIntree) {
        
		if(o == null) {
			if(checkCoverage(pathIntree, "null"))
				return true;
		} 
		else if (Number.class.isAssignableFrom(o.getClass())) {
			Number asNumber = (Number)o;
			if(Double.isNaN(asNumber.doubleValue()) || Float.isNaN(asNumber.floatValue())){
				if(checkCoverage(pathIntree, "NaN"))
					return true;
			}
			else if(Double.isInfinite(asNumber.doubleValue()) && asNumber.doubleValue() > 0 || 
					Float.isInfinite(asNumber.floatValue()) && asNumber.floatValue() > 0){
				if(checkCoverage(pathIntree, "pos-infty"))
					return true;
			}
			else if(Double.isInfinite(asNumber.doubleValue()) && asNumber.doubleValue() < 0 || 
					Float.isInfinite(asNumber.floatValue()) && asNumber.floatValue() < 0){
				if(checkCoverage(pathIntree, "neg-infty"))
					return true;
			} 
			else if(Math.abs(asNumber.doubleValue() - Double.MAX_VALUE) < 1e-6 || 
					Math.abs(asNumber.floatValue() - Float.MAX_VALUE) < 1e-6 ){
				if(checkCoverage(pathIntree, "float-max"))
					return true;
				// this may not work well with min value, it should not be a problem
			} else if(Math.abs(asNumber.doubleValue() - Double.MIN_VALUE) < 1e-6 || 
					Math.abs(asNumber.floatValue() - Float.MIN_VALUE) < 1e-6 ){
				if(checkCoverage(pathIntree, "float-min"))
					return true;
			}
			else if(asNumber.longValue() == -1) {
				if(checkCoverage(pathIntree, "minus-1"))
					return true;
			} else if(asNumber.longValue() == 0) {
				if(checkCoverage(pathIntree, "zero"))
					return true;
			} else if(asNumber.longValue() == Integer.MAX_VALUE) {
				if(checkCoverage(pathIntree, "max-int"))
					return true;
			} else if(asNumber.longValue() == Integer.MIN_VALUE) {
				if(checkCoverage(pathIntree, "min-int"))
					return true;
			} else if(asNumber.longValue() == Long.MAX_VALUE) {
				if(checkCoverage(pathIntree, "max-long"))
					return true;
			} else if(asNumber.longValue() == Long.MIN_VALUE) {
				if(checkCoverage(pathIntree, "min-long"))
					return true;
			} else if(asNumber.longValue() == Byte.MAX_VALUE) {
				if(checkCoverage(pathIntree, "max-byte"))
					return true;
			} else if(asNumber.longValue() == Byte.MIN_VALUE) {
				if(checkCoverage(pathIntree, "min-byte"))
					return true;
			}
		}
		return false;
	}
	public boolean hasFoundSuspiciousValue() {
		return foundSuspiciousValue;
	}
	public void resetFoundSuspiciousValue() {
		this.foundSuspiciousValue = false;
	}
	public Map<List<String>, Set<String>> getCoveredPathToElement() {
		return coveredPathToElement;
	}
	public void setCoveredPathToElement(Map<List<String>, Set<String>> coveredPathToElement) {
		this.coveredPathToElement = coveredPathToElement;
	}
	public Map.Entry<List<String>, String> getLastFoundValue() {
		return lastFoundValue;
	}
	public void setLastFoundValue(Map.Entry<List<String>, String> lastFoundValue) {
		this.lastFoundValue = lastFoundValue;
	}


}
