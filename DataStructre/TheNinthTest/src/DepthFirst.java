public class DepthFirst {
    public static class TwoLinkList {
        TwoLinkList pre;
        int data;
        TwoLinkList next;

        public TwoLinkList() {
            this.pre = null;
            this.data = 0;
            this.next = null;
        }

        public TwoLinkList(TwoLinkList pre, int data, TwoLinkList next) {
            this.pre = pre;
            this.data = data;
            this.next = next;
        }
    }

    public static class Stack {
        private TwoLinkList top;

        public void push(int data) {
            TwoLinkList S = new TwoLinkList();
            S.data = data;
            if (top != null) {
                S.next = top;
                top.pre = S;
            }
            top = S;
        }

        public int pop() {
            if (top == null) {
                System.out.println("栈空，弹出失败");
                return -1;
            }
            int data = top.data;
            if (top.next != null) {
                top.next.pre = null;
            }
            top = top.next;
            return data;
        }

        public boolean isEmpty() {
            return top == null;
        }
    }

    static class AdjListNode {
        int prevLinkId;
        int weight;
        int visitedFlag;
        int nodeId;
        AdjListNode next;

        public AdjListNode(int prevLinkId, int weight, int visitedFlag, int nodeId) {
            this.prevLinkId = prevLinkId;
            this.weight = weight;
            this.visitedFlag = visitedFlag;
            this.nodeId = nodeId;
            this.next = null;
        }
    }

    private AdjListNode[] adjList;

    public void AdjacencyList(int nodeCount) {
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

    public void dfsTraverse(int startNode) {
        if (adjList == null || startNode < 0 || startNode >= adjList.length) {
            System.out.println("图未初始化或起始节点非法");
            return;
        }

        Stack stack = new Stack();
        stack.push(startNode);
        adjList[startNode].visitedFlag = 0;

        System.out.print("深度优先遍历结果：");

        while (!stack.isEmpty()) {
            int currentNode = stack.pop();
            System.out.print(currentNode + " ");

            AdjListNode temp = adjList[currentNode].next;
            while (temp != null) {
                int neighborNode = temp.nodeId;
                if (adjList[neighborNode].visitedFlag == -1) {
                    stack.push(neighborNode);
                    adjList[neighborNode].visitedFlag = 0;
                    temp.visitedFlag = 0;
                }
                temp = temp.next;
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        DepthFirst df = new DepthFirst();

        df.AdjacencyList(5);

        df.addEdge(0, 1, 10);
        df.addEdge(0, 2, 20);
        df.addEdge(1, 3, 30);
        df.addEdge(2, 4, 40);
        df.addEdge(3, 4, 50);

        df.dfsTraverse(0);
    }
}