
import java.util.HashMap;
import java.util.Map;

public class GetHuffmanCodes {


    public static void get() {

        int[][] data = {
                {0, 75675},
                {1, 97},
                {2, 12},
                {3, 13},
                {4, 16},
                {5, 45}
        };
        //构建哈夫曼树
        StaticTernaryHuffmanTree root = BuildHuffmanTree.buildTree(data);

        // 2. 用来记录 “值 -> 哈夫曼编码”
        Map<Integer, String> codeMap = new HashMap<>();

        // 3. 遍历树，生成编码
        buildCode(root, "", codeMap);

        // 4. 按默认数组顺序输出编码
        System.out.println("哈夫曼编码结果：");
        for (int[] d : data) {
            int value = d[0];
            System.out.println(value + " -> " + codeMap.get(value));
        }
    }


    private static void buildCode(StaticTernaryHuffmanTree node,
                                  String code,
                                  Map<Integer, String> codeMap) {

        if (node == null) return;

        // 叶子节点
        if (node.LeftSon == null && node.RightSon == null) {
            // 单节点情况处理（防止 code = ""）
            codeMap.put(node.data, code.isEmpty() ? "0" : code);
            return;
        }

        // 左边记 0
        if (node.LeftSon != null) {
            buildCode(node.LeftSon, code + "0", codeMap);
        }

        // 右边记 1
        if (node.RightSon != null) {
            buildCode(node.RightSon, code + "1", codeMap);
        }
    }

    public static void main(String[] args) {
        get();
    }
}

