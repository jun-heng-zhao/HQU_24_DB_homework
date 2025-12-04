public class StaticTernaryHuffmanTree {
    public int data;
    public StaticTernaryHuffmanTree parent;
    public StaticTernaryHuffmanTree LeftSon;
    public StaticTernaryHuffmanTree RightSon;

    public StaticTernaryHuffmanTree(int data) {
        this.data = data;
        this.parent = null;
        this.LeftSon = null;
        this.RightSon = null;
    }

    public StaticTernaryHuffmanTree(int data,
                                    StaticTernaryHuffmanTree left,
                                    StaticTernaryHuffmanTree right) {
        this.data = data;
        this.LeftSon = left;
        this.RightSon = right;

        if (left != null) left.parent = this;
        if (right != null) right.parent = this;
    }

    public void setParent(StaticTernaryHuffmanTree parent) {
        this.parent = parent;
    }

    public void setLeftSon(StaticTernaryHuffmanTree leftSon) {
        this.LeftSon = leftSon;
    }

    public void setRightSon(StaticTernaryHuffmanTree rightSon) {
        this.RightSon = rightSon;
    }
}
