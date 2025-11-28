import java.util.ArrayList;
import java.util.List;

public class recursion {
    public static TreeNode buildTree(int[] data) {
        if (data == null) {
            return null;
        }
        List<TreeNode> nodes = new ArrayList<>();
        for (int num : data) {
            nodes.add(new TreeNode(num));
        }//创建所有节点并保存到列表

        int totalNodes = nodes.size();

        for (int i = 0; i < totalNodes; i++) {
            TreeNode current = nodes.get(i);
            int leftIndex = 2 * i + 1;
            if (leftIndex < totalNodes) {
                current.setLeft(nodes.get(leftIndex));
            }
            int rightIndex = 2 * i + 2;
            if (rightIndex < totalNodes) {
                current.setRight(nodes.get(rightIndex));
            }
        }// 完全二叉树

        return nodes.getFirst();
    }// 构建树 基本操作是先把传入的数组的所有值放入TreeNode，然后遍历节点，根据子下标的数学特征，判断是否要设置为其子节点，最后返回根节点(借助了List辅助)

    public static void SearchPre(TreeNode node) {
        if (node == null) return;
        System.out.print(node.getData() + " ");
        SearchPre(node.getLeft());
        SearchPre(node.getRight());
    }//递归前缀

    public static void SearchIn(TreeNode node) {
        if (node == null) return;
        SearchIn(node.getLeft());
        System.out.print(node.getData() + " ");
        SearchIn(node.getRight());
    }//递归中缀

    public static void SearchEnd(TreeNode node) {
        if (node == null) return;
        SearchEnd(node.getLeft());
        SearchEnd(node.getRight());
        System.out.print(node.getData() + " ");
    }//递归后缀

    public static void main(String[] args) {
        int[] a = new int[10];
        for (int i = 0; i < 10; i++) {
            a[i] = i;
        }
        TreeNode t = buildTree(a);
        System.out.println("前序遍历:");
        SearchPre(t);
        System.out.println(' ');
        System.out.println("中序遍历:");
        SearchIn(t);
        System.out.println(' ');
        System.out.println("后序遍历:");
        SearchEnd(t);
    }
}
