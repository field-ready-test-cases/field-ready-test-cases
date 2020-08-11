package fieldtest.triggering.parameter_tree;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import fieldtest.aspect.TestStorage;
import fieldtest.logging.SingletonFieldTestLogger;
import fieldtest.triggering.parameter_tree.TestParameterTree.TreeNode;

import org.reflections.ReflectionUtils;

/**
 * A class for exploring the field data that is potentially used for field 
 * testing. Starting from the static fields of 
 * <code>fieldtest.aspect.TestStorage</code> it performs depth-bounded 
 * depth-first exploration. 
 * 
 * The class provides protected hooks named "checkXForStopping" to examine 
 * certain types of data. 
 * 
 */
public class TestParameterTreeTraverser {

	public TestParameterTreeTraverser(int maxDepth, boolean exploreArrays, boolean buildTree) {
		super();
		this.maxDepth = maxDepth;
		this.exploreArrays = exploreArrays;
		this.buildTree = buildTree;
	}

	private int maxDepth = 1;
	private boolean exploreArrays = false;
	private static final Nil<String> emptyPath = new Nil<String>();
	// we may not always want to build the tree
	private boolean buildTree = true;

	// some hooks to work on elements in the object tree
	protected boolean checkComplexObjectForStopping(Object o, Class<?> t, ImmutableList<String> pathIntree) {
		return false;
	}

	protected boolean checkStringForStopping(CharSequence cSeq, Class<?> t, ImmutableList<String> pathIntree) {
		return false;
	}

	protected boolean checkArrayForStopping(Object o, Class<?> t, ImmutableList<String> pathIntree) {
		return false;
	}

	protected boolean checkPrimitiveValueForStopping(Object o, Class<?> t, ImmutableList<String> pathIntree) {
		return false;
	}
	public TestParameterTree traverseTree() {
		TestParameterTree parameterTree = buildTree ? 
				new TestParameterTree() : null;

		Field[] fields = TestStorage.class.getDeclaredFields();

		for (Field f : fields) {
			try {
				f.setAccessible(true);
				Object o = f.get(null);
				Class<?> t = o.getClass(); // f.getType();
				String headName = f.getName() + ":" + t.getCanonicalName();
				ImmutableList<String> pathIntree = new Cons<String>(headName, emptyPath);
				// parentObject = null for static fields on depth one
				if(processObjectRecursively(o, t, buildTree ? 
						parameterTree.getRoot() : null, null, 1, pathIntree))
					break;

			} catch (IllegalAccessException e) {
				SingletonFieldTestLogger.getInstance().warning("We cannot check "
						+ "the test parameters using reflection due to an "
						+ "IllegalAccessException with the message: " + 
						e.getMessage());
				throw new RuntimeException("Cannot handle IllegalAccessException");
			}
		}
		return parameterTree;
	}
	
	private static boolean isBoxedPrimitive(Class<?> clazz) {
	    return clazz.equals(Boolean.class) || 
	        clazz.equals(Integer.class) ||
	        clazz.equals(Character.class) ||
	        clazz.equals(Byte.class) ||
	        clazz.equals(Short.class) ||
	        clazz.equals(Double.class) ||
	        clazz.equals(Long.class) ||
	        clazz.equals(Float.class);
	}

	/**
	 * Traverse tree recursively until one of the check methods returns true, 
	 * signalling that traversal shall be stopped
	 * 
	 * @param o
	 * @param t
	 * @param parentNode
	 * @param parentObject
	 * @param depth
	 * @param pathIntree
	 * @return true if traversal shall be stopped
	 * @throws IllegalAccessException
	 */
	private boolean processObjectRecursively(Object o, Class<?> t, TreeNode parentNode, 
			Object parentObject, int depth,
			ImmutableList<String> pathIntree) throws IllegalAccessException {
		TreeNode newNode = null;

		if (t.isPrimitive() || isBoxedPrimitive(t)) {
			if(buildTree) {
				newNode = new TreeNode(o);
				parentNode.addChild(newNode);
			}
			return checkPrimitiveValueForStopping(o, t, pathIntree);
		} else if (t.isArray()) {
			if(buildTree) {
				newNode = new TreeNode(o);
				parentNode.addChild(newNode);
			}
			if(checkArrayForStopping(o, t, pathIntree))
				return true;

			if (exploreArrays && o != null) {
				int length = Array.getLength(o);
				for (int i = 0; i < length; i++) {
					Object arrayElement = Array.get(o, i);
					// default to Object.class for null elems (it is complicated
					// to get the right type and it should not make a
					// difference)
					Class<?> typeOfElement = getType(arrayElement);
					String newPathElem = "[x] : " + typeOfElement.getCanonicalName();
					ImmutableList<String> newPath = new Cons<String>(newPathElem,
							pathIntree);
					if(processObjectRecursively(arrayElement, typeOfElement, newNode, o, depth + 1, newPath))
						return true;
				}
			}
			return false;
		} else if (CharSequence.class.isAssignableFrom(t)) { // TODO check this again
			if(buildTree) {
				newNode = new TreeNode(o);
				parentNode.addChild(newNode);
			}
			return checkStringForStopping((CharSequence)o, t, pathIntree);
		} else {
			if(buildTree) {
				newNode = new TreeNode(o);
				parentNode.addChild(newNode);
			}
			if(checkComplexObjectForStopping(o, t, pathIntree)){
				return true;
            }
			if (depth < maxDepth && o != null) {
				for (Field fieldOfT : ReflectionUtils.getAllFields(t)) {
					if(Modifier.isFinal(fieldOfT.getModifiers()) && 
							Modifier.isStatic(fieldOfT.getModifiers()))
						continue;
					fieldOfT.setAccessible(true);
					Object childObject = fieldOfT.get(o);
					Class<?> typeOfChild = getType(childObject);//fieldOfT.getType();
					String newPathElem = fieldOfT.getName() + ":" + typeOfChild.getCanonicalName();
					ImmutableList<String> newPath = 
							new Cons<String>(newPathElem, pathIntree);

					if(processObjectRecursively(childObject, typeOfChild, newNode, o, depth + 1, newPath))
						return true;
				}
			}
			// signal that traversal shall not be stopped if we did not decide 
			// to stop before
			return false;
		}
	}

	

	private Class<?> getType(Object object) {
		if (object != null) {
			return object.getClass();
		} else {
			// default to object for null
			return Object.class;
		}
	}

}
