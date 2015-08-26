import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BinaryTreeLevel {

	public static List<List<Integer>> levelOrder(TreeNode root) {
		List<List<Integer>> result = new ArrayList<>();
		
		List<TreeNode> checkingQueue = new ArrayList<>();
		List<TreeNode> nextQueue = new ArrayList<>();
		
		if(root != null) {
			checkingQueue.add(root);
		}
		
		while(!checkingQueue.isEmpty()) {
			List<Integer> levelList = new ArrayList<>();
			for (TreeNode treeNode : checkingQueue) {
				if(treeNode.left != null){
					nextQueue.add(treeNode.left);
				}
				if(treeNode.right != null){
					nextQueue.add(treeNode.right);
				}
				levelList.add(treeNode.val);
			}
			result.add(levelList);
			checkingQueue.clear();
			checkingQueue.addAll(nextQueue);
			nextQueue.clear();
		}
		return result;
	}
	
	public static void main(String[] args) {
		TreeNode root = new TreeNode(1);
//		root.right = new TreeNode(2);
//		root.left = new TreeNode(3);
//		root.right.right = new TreeNode(4);
//		root.right.left = new TreeNode(7);
//		root.left.right = new TreeNode(5);
//		root.left.left = new TreeNode(10);
//		root.left.right.left = new TreeNode(6);
		List<List<Integer>> list = BinaryTreeLevel.levelOrder(root);
		for (List<Integer> list2 : list) {
			System.out.println(Arrays.toString(list2.toArray()));
		}
	}


}

class TreeNode {
	int val;
	TreeNode left;
	TreeNode right;

	TreeNode(int x) {
		val = x;
	}
}
