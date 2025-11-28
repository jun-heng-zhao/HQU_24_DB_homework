public class BreadthFirst {
    // 邻接表节点定义
    static class AdjListNode {
        int prevLinkId;    // 本数据链接的上一个数据
        int weight;        // 本数据点权重
        int visitedFlag;   // 是否被访问标识,默认为-1，表示未被访问，0则是访问
        int nodeId;        // 当前邻接节点ID
        AdjListNode next;  // 下一个邻接节点指针

        public AdjListNode(int prevLinkId, int weight, int visitedFlag, int nodeId) {
            this.prevLinkId = prevLinkId;
            this.weight = weight;
            this.visitedFlag = visitedFlag;
            this.nodeId = nodeId;
            this.next = null;
        }
    }

    // 邻接表数组
    private AdjListNode[] adjList;

    // 队列定义
    private static class Node {
        int data;
        Node next;

        Node() {
            this.data = -1;
            this.next = null;
        }

        Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public boolean isEmpty() {
        return size == 0;
    }

    public void enqueue(int data) {
        Node newNode = new Node(data);
        if (isEmpty()) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
        size++;
    }

    public int dequeue() {
        if (isEmpty()) {
            System.out.println("队列为空");
            return -1;
        }
        Node temp = head;
        int data = temp.data;
        head = head.next;
        size--;
        if (isEmpty()) {
            tail = null;
        }
        return data;
    }

    public void initializeAdjacencyList(int nodeCount) {
        adjList = new AdjListNode[nodeCount];
        for (int i = 0; i < nodeCount; i++) {
            adjList[i] = new AdjListNode(-1, 0, -1, i);
        }
    }

    public void addEdge(int fromNode, int toNode, int weight) {
        AdjListNode newNode = new AdjListNode(fromNode, weight, -1, toNode);
        newNode.next = adjList[fromNode].next;
        adjList[fromNode].next = newNode;
    }

    // 重置所有节点的访问状态
    public void resetVisitedFlags() {
        for (AdjListNode node : adjList) {
            node.visitedFlag = -1;
            AdjListNode temp = node.next;
            while (temp != null) {
                temp.visitedFlag = -1;
                temp = temp.next;
            }
        }
    }

    // 广度优先遍历
    public void bfsTraverse(int startNode) {
        // 重置访问状态
        resetVisitedFlags();

        // 重置队列
        head = null;
        tail = null;
        size = 0;

        System.out.print("广度优先遍历结果：");

        // 起始节点入队并标记已访问
        enqueue(startNode);
        adjList[startNode].visitedFlag = 0;

        while (!isEmpty()) {
            int currentNode = dequeue();
            System.out.print(currentNode + " ");

            // 遍历当前节点的所有邻接节点
            AdjListNode temp = adjList[currentNode].next;
            while (temp != null) {
                int neighborNode = temp.nodeId;
                if (adjList[neighborNode].visitedFlag == -1) {
                    enqueue(neighborNode);
                    adjList[neighborNode].visitedFlag = 0;
                    temp.visitedFlag = 0;
                }
                temp = temp.next;
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        BreadthFirst bf = new BreadthFirst();

        // 初始化邻接表
        bf.initializeAdjacencyList(5);

        // 构建图
        bf.addEdge(0, 1, 10);
        bf.addEdge(0, 2, 20);
        bf.addEdge(1, 3, 30);
        bf.addEdge(2, 4, 40);
        bf.addEdge(3, 4, 50);

        // 执行广度优先遍历（起始节点0）
        bf.bfsTraverse(0);


    }
}