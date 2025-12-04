import java.util.ArrayList;
import java.util.List;

public class warehouse {

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
    }// 构建树

    public static class TwoLinkList {
        TwoLinkList pre;
        TreeNode data;
        TwoLinkList next;

        public TwoLinkList(TwoLinkList pre, TreeNode data, TwoLinkList next) {
            this.pre = pre;
            this.data = data;
            this.next = next;
        }

    }//构建双链表

    public static class Stack {
        private TwoLinkList top;

        public void push(TreeNode data) {
            TwoLinkList S = new TwoLinkList(null, data, null);
            if (top != null) {
                S.next = top;
                top.pre = S;
            }
            top = S;
        }//一个对栈顶进行压入的操作，插入双链表的头

        public TreeNode pop() {
            if (top == null) {
                System.out.println("栈空，弹出失败");
                return null;
            }
            TreeNode data = top.data;
            top = top.next;
            return data;
        }//弹出栈顶数据

        public boolean isEmpty() {
            return top != null;
        }//判断栈顶是否空
    }//对栈的一系列操作

    public static void PreFound(TreeNode root){
        if (root == null) {
            System.out.println("树为空");
            return;
        }

        Stack stack = new Stack();
        stack.push(root);

        System.out.println("先序遍历:");

        while (stack.isEmpty()) {
            //输出并弹出根
            TreeNode node = stack.pop();
            System.out.print(node.getData() + " ");

            // 先压右子树，再压左子树
            if (node.getRight() != null) {
                stack.push(node.getRight());
            }
            if (node.getLeft() != null) {
                stack.push(node.getLeft());
            }
        }
        System.out.println();
    }

    public static void InFound(TreeNode root){
        if (root == null) {
            System.out.println("树为空");
            return;
        }

        Stack stack = new Stack();
        TreeNode current = root;

        System.out.println("中序遍历:");

        while (current != null || stack.isEmpty()) {
            // 遍历到最左边的节点，沿途压入栈
            while (current != null) {
                stack.push(current);
                current = current.getLeft();
            }

            //根
            current = stack.pop();
            System.out.print(current.getData() + " ");

            // 转向右子树，相当于把树拆分成两个树，遍历完左边的树，对右边的进行操作
            current = current.getRight();
        }
        System.out.println();
    }

    public static void NextFound(TreeNode root){
        if (root == null) {
            System.out.println("树为空");
            return;
        }

        Stack stack = new Stack();
        Stack resultStack = new Stack(); // 辅助栈，用于翻转结果

        stack.push(root);

        System.out.println("后序遍历:");

        while (stack.isEmpty()) {
            TreeNode node = stack.pop();
            resultStack.push(node); // 将节点压入结果栈

            // 先左后右，这样结果栈弹出时就是右左根，翻转后就是左右根
            if (node.getLeft() != null) {
                stack.push(node.getLeft());
            }
            if (node.getRight() != null) {
                stack.push(node.getRight());
            }
        }

        // 从结果栈中弹出所有节点，实现翻转
        while (resultStack.isEmpty()) {
            TreeNode node = resultStack.pop();
            System.out.print(node.getData() + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[] a = new int[10];
        for (int i = 0; i < 10; i++) {
            a[i] = i;
        }
        TreeNode t = buildTree(a);//构建一个树

        PreFound(t);  // 先序
        InFound(t);   // 中序
        NextFound(t); // 后序
    }
}
