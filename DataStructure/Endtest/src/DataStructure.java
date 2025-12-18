// ==================== 数据结构定义 ====================
class ListNode {
    int val;
    ListNode next;

    ListNode(int val) {
        this.val = val;
        this.next = null;
    }
}

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int val) {
        this.val = val;
        this.left = null;
        this.right = null;
    }
}

// ==================== 算法工具类集合 ====================

// 0. 随机数组生成工具类
class RandomArrayUtils {

    /**
     * 生成指定范围和长度的随机整数数组
     *
     * @param size  数组大小
     * @param min   最小值（包含）
     * @param max   最大值（包含）
     * @return 随机整数数组
     */
    public static int[] generateRandomArray(int size, int min, int max) {
        if (size <= 0) {
            return new int[0];
        }

        int[] array = new int[size];
        java.util.Random random = new java.util.Random();

        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(max - min + 1) + min;
        }

        return array;
    }

    /**
     * 生成已排序的随机数组
     *
     * @param size  数组大小
     * @param min   最小值（包含）
     * @param max   最大值（包含）
     * @return 已排序的随机整数数组
     */
    public static int[] generateSortedRandomArray(int size, int min, int max) {
        int[] array = generateRandomArray(size, min, max);
        java.util.Arrays.sort(array);
        return array;
    }

    /**
     * 生成用于二叉排序树的随机数组（无重复值）
     *
     * @param size  数组大小
     * @param min   最小值（包含）
     * @param max   最大值（包含）
     * @return 无重复值的随机整数数组
     */
    public static int[] generateBSTArray(int size, int min, int max) {
        if (size <= 0 || (max - min + 1) < size) {
            // 如果范围不足以生成不重复的值，返回空数组
            return new int[0];
        }

        java.util.Random random = new java.util.Random();
        java.util.Set<Integer> set = new java.util.HashSet<>();

        while (set.size() < size) {
            set.add(random.nextInt(max - min + 1) + min);
        }

        int[] array = new int[size];
        int i = 0;
        for (int num : set) {
            array[i++] = num;
        }

        return array;
    }

    /**
     * 生成随机目标值
     *
     * @param min 最小值（包含）
     * @param max 最大值（包含）
     * @return 随机整数
     */
    public static int generateRandomTarget(int min, int max) {
        java.util.Random random = new java.util.Random();
        return random.nextInt(max - min + 1) + min;
    }
}

// 1. 逆转链表工具类
class ReverseListUtils {

    /**
     * 逆转单链表（迭代版本）
     *
     * @param head 链表头节点
     * @return 逆转后的链表头节点
     */
    public static ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;

        while (curr != null) {
            ListNode nextTemp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = nextTemp;
        }

        return prev;
    }

    /**
     * 逆转单链表并返回节点值数组
     *
     * @param head 链表头节点
     * @return 逆转后链表的节点值数组
     */
    public static int[] reverseListToArray(ListNode head) {
        ListNode reversed = reverseList(head);
        java.util.List<Integer> list = new java.util.ArrayList<>();
        ListNode current = reversed;

        while (current != null) {
            list.add(current.val);
            current = current.next;
        }

        return list.stream().mapToInt(i -> i).toArray();
    }

    /**
     * 从数组构建链表
     *
     * @param values 节点值数组
     * @return 链表头节点
     */
    public static ListNode buildFromArray(int[] values) {
        if (values == null || values.length == 0) {
            return null;
        }

        ListNode head = new ListNode(values[0]);
        ListNode current = head;

        for (int i = 1; i < values.length; i++) {
            current.next = new ListNode(values[i]);
            current = current.next;
        }

        return head;
    }
}

// 2. 带尾指针的单链表工具类
class LinkedListWithTailUtils {

    /**
     * 从数组构建带尾指针的链表
     *
     * @param values 数组
     * @return 包含头节点和尾节点的数组 [head, tail]
     */
    public static Object[] buildFromArray(int[] values) {
        if (values == null || values.length == 0) {
            return new Object[]{null, null};
        }

        ListNode head = new ListNode(values[0]);
        ListNode tail = head;

        for (int i = 1; i < values.length; i++) {
            ListNode newNode = new ListNode(values[i]);
            tail.next = newNode;
            tail = newNode;
        }

        return new Object[]{head, tail};
    }

    /**
     * 获取链表的值数组
     *
     * @param head 链表头节点
     * @return 链表节点值数组
     */
    public static int[] getListValues(ListNode head) {
        java.util.List<Integer> list = new java.util.ArrayList<>();
        ListNode current = head;

        while (current != null) {
            list.add(current.val);
            current = current.next;
        }

        return list.stream().mapToInt(i -> i).toArray();
    }
}

// 3. KMP算法工具类（输出next数组和nextVal数组，以及匹配结果）
class KMPUtils {

    /**
     * 计算KMP算法的next数组
     *
     * @param pattern 模式字符串
     * @return next数组
     */
    public static int[] computeNext(String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            return new int[0];
        }

        int[] next = new int[pattern.length()];
        next[0] = -1;
        int i = 0, j = -1;

        while (i < pattern.length() - 1) {
            if (j == -1 || pattern.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                next[i] = j;
            } else {
                j = next[j];
            }
        }

        return next;
    }

    /**
     * 计算KMP算法的改进next数组
     *
     * @param pattern 模式字符串
     * @return 改进的next数组
     */
    public static int[] computeNextVal(String pattern) {
        int[] next = computeNext(pattern);
        int[] nextVal = new int[pattern.length()];

        if (!pattern.isEmpty()) {
            nextVal[0] = -1;
        }

        for (int i = 1; i < pattern.length(); i++) {
            if (pattern.charAt(i) == pattern.charAt(next[i])) {
                nextVal[i] = nextVal[next[i]];
            } else {
                nextVal[i] = next[i];
            }
        }

        return nextVal;
    }

    /**
     * KMP算法匹配，返回匹配结果数组
     *
     * @param text    主文本字符串
     * @param pattern 模式字符串
     * @return 结果数组：[匹配次数, 匹配位置1, 匹配位置2, ...]
     */
    public static int[] kmpSearchAll(String text, String pattern) {
        if (text == null || pattern == null || pattern.isEmpty()) {
            return new int[]{0};
        }

        int[] nextVal = computeNextVal(pattern);
        java.util.List<Integer> positions = new java.util.ArrayList<>();
        int i = 0, j = 0;
        int matchCount = 0;

        while (i < text.length()) {
            if (j == -1 || text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            } else {
                j = nextVal[j];
            }

            if (j == pattern.length()) {
                positions.add(i - j);
                matchCount++;
                j = nextVal[j - 1];
                if (j == -1) j = 0;
            }
        }

        int[] result = new int[positions.size() + 1];
        result[0] = matchCount;
        for (int k = 0; k < positions.size(); k++) {
            result[k + 1] = positions.get(k);
        }

        return result;
    }

    /**
     * 执行KMP算法并返回所有结果
     *
     * @param text    主文本字符串
     * @param pattern 模式字符串
     * @return 包含所有结果的数组：[next数组, nextVal数组, 匹配结果数组]
     */
    public static Object[] executeKMP(String text, String pattern) {
        int[] next = computeNext(pattern);
        int[] nextVal = computeNextVal(pattern);
        int[] matchResult = kmpSearchAll(text, pattern);

        return new Object[]{next, nextVal, matchResult};
    }
}

// 4. 二叉树遍历工具类（使用栈）
class BinaryTreeTraversalUtils {

    /**
     * 二叉树前序遍历（使用栈）并返回数组
     *
     * @param root 二叉树根节点
     * @return 遍历结果的整数数组
     */
    public static int[] preorderWithStack(TreeNode root) {
        if (root == null) return new int[0];

        java.util.List<Integer> result = new java.util.ArrayList<>();
        java.util.Stack<TreeNode> stack = new java.util.Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            result.add(node.val);

            if (node.right != null) {
                stack.push(node.right);
            }
            if (node.left != null) {
                stack.push(node.left);
            }
        }

        return result.stream().mapToInt(i -> i).toArray();
    }

    /**
     * 二叉树中序遍历（使用栈）并返回数组
     *
     * @param root 二叉树根节点
     * @return 遍历结果的整数数组
     */
    public static int[] inorderWithStack(TreeNode root) {
        if (root == null) return new int[0];

        java.util.List<Integer> result = new java.util.ArrayList<>();
        java.util.Stack<TreeNode> stack = new java.util.Stack<>();
        TreeNode current = root;

        while (current != null || !stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                current = current.left;
            }

            current = stack.pop();
            result.add(current.val);
            current = current.right;
        }

        return result.stream().mapToInt(i -> i).toArray();
    }

    /**
     * 二叉树后序遍历（使用栈）并返回数组
     *
     * @param root 二叉树根节点
     * @return 遍历结果的整数数组
     */
    public static int[] postorderWithStack(TreeNode root) {
        if (root == null) return new int[0];

        java.util.List<Integer> result = new java.util.ArrayList<>();
        java.util.Stack<TreeNode> stack1 = new java.util.Stack<>();
        java.util.Stack<TreeNode> stack2 = new java.util.Stack<>();
        stack1.push(root);

        while (!stack1.isEmpty()) {
            TreeNode node = stack1.pop();
            stack2.push(node);

            if (node.left != null) {
                stack1.push(node.left);
            }
            if (node.right != null) {
                stack1.push(node.right);
            }
        }

        while (!stack2.isEmpty()) {
            result.add(stack2.pop().val);
        }

        return result.stream().mapToInt(i -> i).toArray();
    }

    /**
     * 执行二叉树三种遍历并返回所有结果
     *
     * @param root 二叉树根节点
     * @return 包含三种遍历结果的数组：[前序, 中序, 后序]
     */
    public static Object[] executeAllTraversals(TreeNode root) {
        int[] preorder = preorderWithStack(root);
        int[] inorder = inorderWithStack(root);
        int[] postorder = postorderWithStack(root);

        return new Object[]{preorder, inorder, postorder};
    }

    /**
     * 根据前序和中序序列构建二叉树
     *
     * @param preorder 前序遍历序列
     * @param inorder  中序遍历序列
     * @return 构建的二叉树根节点
     */
    public static TreeNode buildFromPreIn(int[] preorder, int[] inorder) {
        if (preorder == null || inorder == null || preorder.length != inorder.length) {
            return null;
        }
        return buildTreeFromPreIn(preorder, 0, preorder.length - 1, inorder, 0, inorder.length - 1);
    }

    private static TreeNode buildTreeFromPreIn(int[] preorder, int preStart, int preEnd,
                                               int[] inorder, int inStart, int inEnd) {
        if (preStart > preEnd || inStart > inEnd) {
            return null;
        }

        int rootValue = preorder[preStart];
        TreeNode root = new TreeNode(rootValue);

        int rootIndex = -1;
        for (int i = inStart; i <= inEnd; i++) {
            if (inorder[i] == rootValue) {
                rootIndex = i;
                break;
            }
        }

        int leftTreeSize = rootIndex - inStart;

        root.left = buildTreeFromPreIn(preorder, preStart + 1, preStart + leftTreeSize,
                inorder, inStart, rootIndex - 1);
        root.right = buildTreeFromPreIn(preorder, preStart + leftTreeSize + 1, preEnd,
                inorder, rootIndex + 1, inEnd);

        return root;
    }

    /**
     * 根据中序和后序序列构建二叉树
     *
     * @param inorder   中序遍历序列
     * @param postorder 后序遍历序列
     * @return 构建的二叉树根节点
     */
    public static TreeNode buildFromInPost(int[] inorder, int[] postorder) {
        if (inorder == null || postorder == null || inorder.length != postorder.length) {
            return null;
        }
        return buildTreeFromInPost(inorder, 0, inorder.length - 1,
                postorder, 0, postorder.length - 1);
    }

    private static TreeNode buildTreeFromInPost(int[] inorder, int inStart, int inEnd,
                                                int[] postorder, int postStart, int postEnd) {
        if (inStart > inEnd || postStart > postEnd) {
            return null;
        }

        int rootValue = postorder[postEnd];
        TreeNode root = new TreeNode(rootValue);

        int rootIndex = -1;
        for (int i = inStart; i <= inEnd; i++) {
            if (inorder[i] == rootValue) {
                rootIndex = i;
                break;
            }
        }

        int leftTreeSize = rootIndex - inStart;

        root.left = buildTreeFromInPost(inorder, inStart, rootIndex - 1,
                postorder, postStart, postStart + leftTreeSize - 1);
        root.right = buildTreeFromInPost(inorder, rootIndex + 1, inEnd,
                postorder, postStart + leftTreeSize, postEnd - 1);

        return root;
    }

    /**
     * 通过层序遍历数组构建二叉树
     *
     * @param levelOrder 层序遍历数组，null表示空节点
     * @return 构建的二叉树根节点
     */
    public static TreeNode buildFromLevelOrder(Integer[] levelOrder) {
        if (levelOrder == null || levelOrder.length == 0) {
            return null;
        }

        TreeNode root = new TreeNode(levelOrder[0]);
        java.util.Queue<TreeNode> queue = new java.util.LinkedList<>();
        queue.offer(root);

        int i = 1;
        while (!queue.isEmpty() && i < levelOrder.length) {
            TreeNode current = queue.poll();

            // 构建左子节点
            if (i < levelOrder.length && levelOrder[i] != null) {
                current.left = new TreeNode(levelOrder[i]);
                queue.offer(current.left);
            }
            i++;

            // 构建右子节点
            if (i < levelOrder.length && levelOrder[i] != null) {
                current.right = new TreeNode(levelOrder[i]);
                queue.offer(current.right);
            }
            i++;
        }

        return root;
    }
}

// 5. 二分查找工具类
class BinarySearchUtils {

    /**
     * 二分查找（迭代版本）并返回结果
     *
     * @param arr    已排序的数组
     * @param target 目标值
     * @return 结果数组：[是否找到(0/1), 找到的位置]
     */
    public static int[] binarySearchIterative(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (arr[mid] == target) {
                return new int[]{1, mid};
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return new int[]{0, -1};
    }

    /**
     * 二分查找（递归版本）并返回结果
     *
     * @param arr    已排序的数组
     * @param target 目标值
     * @return 结果数组：[是否找到(0/1), 找到的位置]
     */
    public static int[] binarySearchRecursive(int[] arr, int target) {
        int result = binarySearchHelper(arr, target, 0, arr.length - 1);
        if (result >= 0) {
            return new int[]{1, result};
        } else {
            return new int[]{0, -1};
        }
    }

    private static int binarySearchHelper(int[] arr, int target, int left, int right) {
        if (left > right) return -1;

        int mid = left + (right - left) / 2;

        if (arr[mid] == target) {
            return mid;
        } else if (arr[mid] < target) {
            return binarySearchHelper(arr, target, mid + 1, right);
        } else {
            return binarySearchHelper(arr, target, left, mid - 1);
        }
    }
}

// 6. 二叉排序树查找工具类
class BSTSearchUtils {

    /**
     * 二叉排序树查找（递归）并返回结果
     *
     * @param root   二叉排序树根节点
     * @param target 目标值
     * @return 结果数组：[是否找到(0/1), 找到的节点值]
     */
    public static int[] searchRecursive(TreeNode root, int target) {
        TreeNode found = searchHelper(root, target);
        if (found != null) {
            return new int[]{1, found.val};
        } else {
            return new int[]{0, -1};
        }
    }

    private static TreeNode searchHelper(TreeNode root, int target) {
        if (root == null || root.val == target) {
            return root;
        }

        if (target < root.val) {
            return searchHelper(root.left, target);
        } else {
            return searchHelper(root.right, target);
        }
    }

    /**
     * 向二叉排序树插入节点
     *
     * @param root 二叉排序树根节点
     * @param val  要插入的值
     * @return 插入后的根节点
     */
    public static TreeNode insert(TreeNode root, int val) {
        if (root == null) {
            return new TreeNode(val);
        }

        if (val < root.val) {
            root.left = insert(root.left, val);
        } else if (val > root.val) {
            root.right = insert(root.right, val);
        }
        // 如果值相等，不插入（二叉排序树不允许重复值）

        return root;
    }

    /**
     * 从数组构建二叉排序树
     *
     * @param values 要插入的值数组
     * @return 二叉排序树的根节点
     */
    public static TreeNode buildFromArray(int[] values) {
        TreeNode root = null;
        for (int val : values) {
            root = insert(root, val);
        }
        return root;
    }
}

// 7. 希尔排序工具类
class ShellSortUtils {

    /**
     * 希尔排序并返回排序后的数组
     *
     * @param arr 待排序数组
     * @return 排序后的数组
     */
    public static int[] shellSort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return arr == null ? null : arr.clone();
        }

        int[] sorted = arr.clone();
        int n = sorted.length;

        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                int temp = sorted[i];
                int j = i;

                while (j >= gap && sorted[j - gap] > temp) {
                    sorted[j] = sorted[j - gap];
                    j -= gap;
                }

                sorted[j] = temp;
            }
        }

        return sorted;
    }
}

// 8. 快速排序工具类
class QuickSortUtils {

    /**
     * 快速排序并返回排序后的数组
     *
     * @param arr 待排序数组
     * @return 排序后的数组
     */
    public static int[] quickSort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return arr == null ? null : arr.clone();
        }

        int[] sorted = arr.clone();
        quickSortHelper(sorted, 0, sorted.length - 1);
        return sorted;
    }

    private static void quickSortHelper(int[] arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high);
            quickSortHelper(arr, low, pivotIndex - 1);
            quickSortHelper(arr, pivotIndex + 1, high);
        }
    }

    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[low];
        int i = low + 1;
        int j = high;

        while (i <= j) {
            while (i <= j && arr[i] < pivot) i++;
            while (i <= j && arr[j] > pivot) j--;

            if (i <= j) {
                swap(arr, i, j);
                i++;
                j--;
            }
        }

        swap(arr, low, j);
        return j;
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}

// 9. 堆排序工具类
class HeapSortUtils {

    /**
     * 堆排序并返回排序后的数组
     *
     * @param arr 待排序数组
     * @return 排序后的数组
     */
    public static int[] heapSort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return arr == null ? null : arr.clone();
        }

        int[] sorted = arr.clone();
        int n = sorted.length;

        // 构建最大堆
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(sorted, n, i);
        }

        // 逐个将最大元素移到数组末尾
        for (int i = n - 1; i > 0; i--) {
            swap(sorted, 0, i);
            heapify(sorted, i, 0);
        }

        return sorted;
    }

    private static void heapify(int[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && arr[left] > arr[largest]) largest = left;
        if (right < n && arr[right] > arr[largest]) largest = right;

        if (largest != i) {
            swap(arr, i, largest);
            heapify(arr, n, largest);
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}

// ==================== 总调用方法类 ====================
public class DataStructure {

    /**
     * 测试所有算法的总方法
     */
    public static void main(String[] args) {
        System.out.println("========== 算法操作示例开始 ==========\n");

        // 演示所有算法的操作示例
        demonstrateAlgorithms();

        System.out.println("\n========== 算法操作示例结束 ==========");
    }

    /**
     * 演示所有算法的操作示例
     */
    private static void demonstrateAlgorithms() {
        System.out.println("=== 1. 逆转链表操作示例 ===");
        demonstrateReverseList();

        System.out.println("\n=== 2. 带尾指针单链表操作示例 ===");
        demonstrateLinkedListWithTail();

        System.out.println("\n=== 3. KMP算法操作示例 ===");
        demonstrateKMP();

        System.out.println("\n=== 4. 二叉树遍历操作示例 ===");
        demonstrateBinaryTreeTraversal();

        System.out.println("\n=== 5. 二叉树构建操作示例 ===");
        demonstrateBinaryTreeBuild();

        System.out.println("\n=== 6. 二分查找操作示例 ===");
        demonstrateBinarySearch();

        System.out.println("\n=== 7. 二叉排序树操作示例 ===");
        demonstrateBSTSearch();

        System.out.println("\n=== 8. 希尔排序操作示例 ===");
        demonstrateShellSort();

        System.out.println("\n=== 9. 快速排序操作示例 ===");
        demonstrateQuickSort();

        System.out.println("\n=== 10. 堆排序操作示例 ===");
        demonstrateHeapSort();
    }

    /**
     * 逆转链表操作示例
     */
    private static void demonstrateReverseList() {
        // 1. 使用随机数组创建链表
        int[] randomArray = RandomArrayUtils.generateRandomArray(8, 1, 100);
        ListNode head = ReverseListUtils.buildFromArray(randomArray);

        // 2. 执行逆转操作
        int[] reversed = ReverseListUtils.reverseListToArray(head);

        // 3. 输出结果
        System.out.println("随机数组: " + java.util.Arrays.toString(randomArray));
        System.out.println("逆转后值: " + java.util.Arrays.toString(reversed));
    }

    /**
     * 带尾指针单链表操作示例
     */
    private static void demonstrateLinkedListWithTail() {
        // 1. 使用随机数组创建链表
        int[] randomArray = RandomArrayUtils.generateRandomArray(7, 10, 99);
        Object[] listResult = LinkedListWithTailUtils.buildFromArray(randomArray);

        ListNode head = (ListNode) listResult[0];
        ListNode tail = (ListNode) listResult[1];

        // 2. 获取链表值
        int[] listValues = LinkedListWithTailUtils.getListValues(head);

        // 3. 输出结果
        System.out.println("随机数组: " + java.util.Arrays.toString(randomArray));
        System.out.println("链表值数组: " + java.util.Arrays.toString(listValues));
        System.out.println("尾节点值: " + (tail != null ? tail.val : "null"));
    }

    /**
     * KMP算法操作示例
     */
    private static void demonstrateKMP() {
        // 1. 设置输入数据（KMP算法需要使用特定字符串，不使用随机数组）
        String text = "ABABDABACDABABCABAB";
        String pattern = "ABABCABAB";

        // 2. 执行KMP算法
        Object[] kmpResults = KMPUtils.executeKMP(text, pattern);

        // 3. 提取结果
        int[] next = (int[]) kmpResults[0];
        int[] nextVal = (int[]) kmpResults[1];
        int[] matchResult = (int[]) kmpResults[2];

        // 4. 输出结果
        System.out.println("主文本: " + text);
        System.out.println("模式串: " + pattern);
        System.out.println("Next数组: " + java.util.Arrays.toString(next));
        System.out.println("NextVal数组: " + java.util.Arrays.toString(nextVal));
        System.out.println("匹配次数: " + matchResult[0]);
        System.out.println("匹配位置: " + java.util.Arrays.toString(
                java.util.Arrays.copyOfRange(matchResult, 1, matchResult.length)));
    }

    /**
     * 二叉树遍历操作示例
     */
    private static void demonstrateBinaryTreeTraversal() {
        // 1. 创建二叉树 - 通过随机数组构建（层序遍历数组）
        int[] randomArray = RandomArrayUtils.generateRandomArray(7, 1, 50);

        // 将int数组转换为Integer数组
        Integer[] levelOrder = new Integer[randomArray.length];
        for (int i = 0; i < randomArray.length; i++) {
            levelOrder[i] = randomArray[i];
        }

        TreeNode root = BinaryTreeTraversalUtils.buildFromLevelOrder(levelOrder);

        // 2. 执行三种栈遍历
        Object[] traversalResults = BinaryTreeTraversalUtils.executeAllTraversals(root);

        // 3. 提取结果
        int[] preorder = (int[]) traversalResults[0];
        int[] inorder = (int[]) traversalResults[1];
        int[] postorder = (int[]) traversalResults[2];

        // 4. 输出结果
        System.out.println("随机数组构建的二叉树遍历结果:");
        System.out.println("随机数组: " + java.util.Arrays.toString(randomArray));
        System.out.println("前序遍历: " + java.util.Arrays.toString(preorder));
        System.out.println("中序遍历: " + java.util.Arrays.toString(inorder));
        System.out.println("后序遍历: " + java.util.Arrays.toString(postorder));
    }

    /**
     * 二叉树构建操作示例
     */
    private static void demonstrateBinaryTreeBuild() {
        // 1. 设置输入序列 - 通过两个数组构建（使用固定序列，不使用随机数组）
        int[] preorder = {1, 2, 4, 5, 3, 6, 7};
        int[] inorder = {4, 2, 5, 1, 6, 3, 7};
        int[] postorder = {4, 5, 2, 6, 7, 3, 1};

        // 2. 执行构建操作
        TreeNode tree1 = BinaryTreeTraversalUtils.buildFromPreIn(preorder, inorder);
        TreeNode tree2 = BinaryTreeTraversalUtils.buildFromInPost(inorder, postorder);

        // 3. 验证构建结果
        int[] preorderResult1 = BinaryTreeTraversalUtils.preorderWithStack(tree1);
        int[] inorderResult2 = BinaryTreeTraversalUtils.inorderWithStack(tree2);

        // 4. 输出结果
        System.out.println("固定序列构建二叉树结果:");
        System.out.println("前序+中序构建结果的前序遍历: " + java.util.Arrays.toString(preorderResult1));
        System.out.println("中序+后序构建结果的中序遍历: " + java.util.Arrays.toString(inorderResult2));
    }

    /**
     * 二分查找操作示例
     */
    private static void demonstrateBinarySearch() {
        // 1. 设置输入数据 - 使用随机有序数组
        int[] sortedArray = RandomArrayUtils.generateSortedRandomArray(15, 1, 100);
        int target = RandomArrayUtils.generateRandomTarget(1, 100);

        // 2. 执行查找操作
        int[] resultIterative = BinarySearchUtils.binarySearchIterative(sortedArray, target);
        int[] resultRecursive = BinarySearchUtils.binarySearchRecursive(sortedArray, target);

        // 3. 输出结果
        System.out.println("随机有序数组: " + java.util.Arrays.toString(sortedArray));
        System.out.println("随机查找目标: " + target);
        System.out.println("迭代查找结果: [是否找到: " + resultIterative[0] + ", 位置: " + resultIterative[1] + "]");
        System.out.println("递归查找结果: [是否找到: " + resultRecursive[0] + ", 位置: " + resultRecursive[1] + "]");

        // 为了演示找到的情况，我们再尝试在数组中查找一个实际存在的值
        if (sortedArray.length > 0) {
            int existingTarget = sortedArray[sortedArray.length / 2]; // 取中间的值，大概率存在
            int[] resultExisting = BinarySearchUtils.binarySearchIterative(sortedArray, existingTarget);
            System.out.println("\n查找数组中存在的值: " + existingTarget);
            System.out.println("查找结果: [是否找到: " + resultExisting[0] + ", 位置: " + resultExisting[1] + "]");
        }
    }

    /**
     * 二叉排序树操作示例
     */
    private static void demonstrateBSTSearch() {
        // 1. 创建二叉排序树 - 使用随机数组构建
        int[] randomArray = RandomArrayUtils.generateBSTArray(10, 1, 100);

        // 如果生成的数组为空（范围不足以生成不重复的值），使用一个小范围的示例
        if (randomArray.length == 0) {
            randomArray = RandomArrayUtils.generateRandomArray(7, 10, 50);
        }

        TreeNode bstRoot = BSTSearchUtils.buildFromArray(randomArray);

        // 2. 从数组中随机选择一个值作为查找目标
        int target;
        if (randomArray.length > 0) {
            java.util.Random random = new java.util.Random();
            target = randomArray[random.nextInt(randomArray.length)];
        } else {
            target = 25; // 默认值
        }

        int[] searchResult = BSTSearchUtils.searchRecursive(bstRoot, target);

        // 3. 获取中序遍历验证
        int[] inorder = BinaryTreeTraversalUtils.inorderWithStack(bstRoot);

        // 4. 输出结果
        System.out.println("随机数组: " + java.util.Arrays.toString(randomArray));
        System.out.println("二叉排序树中序遍历: " + java.util.Arrays.toString(inorder));
        System.out.println("查找目标: " + target);
        System.out.println("查找结果: [是否找到: " + searchResult[0] + ", 节点值: " + searchResult[1] + "]");
    }

    /**
     * 希尔排序操作示例
     */
    private static void demonstrateShellSort() {
        // 1. 设置输入数据 - 使用随机数组
        int[] randomArray = RandomArrayUtils.generateRandomArray(12, 1, 100);

        // 2. 执行排序操作
        int[] sortedArray = ShellSortUtils.shellSort(randomArray);

        // 3. 输出结果
        System.out.println("随机数组: " + java.util.Arrays.toString(randomArray));
        System.out.println("希尔排序后: " + java.util.Arrays.toString(sortedArray));
    }

    /**
     * 快速排序操作示例
     */
    private static void demonstrateQuickSort() {
        // 1. 设置输入数据 - 使用随机数组
        int[] randomArray = RandomArrayUtils.generateRandomArray(12, 1, 100);

        // 2. 执行排序操作
        int[] sortedArray = QuickSortUtils.quickSort(randomArray);

        // 3. 输出结果
        System.out.println("随机数组: " + java.util.Arrays.toString(randomArray));
        System.out.println("快速排序后: " + java.util.Arrays.toString(sortedArray));
    }

    /**
     * 堆排序操作示例
     */
    private static void demonstrateHeapSort() {
        // 1. 设置输入数据 - 使用随机数组
        int[] randomArray = RandomArrayUtils.generateRandomArray(12, 1, 100);

        // 2. 执行排序操作
        int[] sortedArray = HeapSortUtils.heapSort(randomArray);

        // 3. 输出结果
        System.out.println("随机数组: " + java.util.Arrays.toString(randomArray));
        System.out.println("堆排序后: " + java.util.Arrays.toString(sortedArray));
    }
}