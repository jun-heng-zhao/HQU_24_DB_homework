import java.util.ArrayList;
import java.util.List;

public class BuildHuffmanTree {

    //传入二维数组，第一个值为需要编码的值，第二个为权重
    public static StaticTernaryHuffmanTree buildTree(int[][] data) {
        if (data == null) {
            return null;
        }

        class node {
            int weight;
            StaticTernaryHuffmanTree treenode;

            node(int value, int weight) {
                this.weight = weight;
                this.treenode = new StaticTernaryHuffmanTree(value);
            }

            node(node left, node right) {
                this.weight = left.weight + right.weight;
                this.treenode = new StaticTernaryHuffmanTree(-1);

                this.treenode.setLeftSon(left.treenode);
                this.treenode.setRightSon(right.treenode);
                left.treenode.setParent(this.treenode);
                right.treenode.setParent(this.treenode);
            }
        }

        // 1. 把所有叶子加入列表
        List<node> list = new ArrayList<>();
        for (int[] d : data) {
            list.add(new node(d[0], d[1]));
        }

        // 2. 不断合并直到只剩一个节点
        while (list.size() > 1) {
            int min1 = -1, min2 = -1;

            // 找最小与次小
            for (int i = 0; i < list.size(); i++) {
                if (min1 == -1 || list.get(i).weight < list.get(min1).weight) {
                    min2 = min1;
                    min1 = i;
                } else if (min2 == -1 || list.get(i).weight < list.get(min2).weight) {
                    min2 = i;
                }
            }

            node a = list.get(min1);
            node b = list.get(min2);

            // 删除两个节点（先删大的）
            int A = Math.min(min1, min2);
            int B = Math.max(min1, min2);
            list.remove(B);
            list.remove(A);

            // 合并
            list.add(new node(a, b));
        }

        return list.get(0).treenode;
    }

}
