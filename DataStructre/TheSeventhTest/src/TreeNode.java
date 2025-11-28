public class TreeNode {
  int data;
 TreeNode left;
 TreeNode right;

    // 构造方法,初始化节点数据
    public TreeNode(int data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }

    public TreeNode(int data, TreeNode left, TreeNode right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }

    public int getData() {
        return data;
    }

    // 已有的获取子节点方法
    public TreeNode getLeft() {
        return left;
    }

    public TreeNode getRight() {
        return right;

    }

    public void setRight(TreeNode right) {
        this.right = right;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

}
