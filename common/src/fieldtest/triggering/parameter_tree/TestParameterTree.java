package fieldtest.triggering.parameter_tree;

import java.util.ArrayList;
import java.util.List;
/**
 * A tree representation of the field data potentially used for field testing. 
 * This is generally a snapshot of the object tree rooted at the 
 * <code>fieldtest.aspect.TestStorage</code> class.
 */
public class TestParameterTree {
	public static class TreeNode{
		private Object value = null;
		private List<TreeNode> children = new ArrayList<TreeNode>();
		
		public TreeNode(Object value) {
			this.setValue(value);
		}

		public void addChild(TreeNode e) {
			this.children.add(e);
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}
	}
	private TreeNode root;
	public TestParameterTree(){
		this.setRoot(new TreeNode(null));
	}
	public TreeNode getRoot() {
		return root;
	}
	public void setRoot(TreeNode root) {
		this.root = root;
	}
}
