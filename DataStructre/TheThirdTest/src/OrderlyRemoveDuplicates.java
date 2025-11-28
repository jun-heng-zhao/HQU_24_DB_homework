//去重，模式串手写
public class OrderlyRemoveDuplicates {
    public static class listNode {
        int data;
        OrderlyRemoveDuplicates.listNode next;

        public listNode(int a) {
            this.data = a;
            this.next = null;
        }

    }
    public static void removeDuplicates(OrderlyRemoveDuplicates.listNode head) {
        if (head == null) {
            return; // 空链表无需处理
        }

        boolean[] seen = new boolean[101];
        seen[head.data] = true;

        OrderlyRemoveDuplicates.listNode prev = head;
        OrderlyRemoveDuplicates.listNode current = head.next;

        while (current != null) {
            int data = current.data;
            if (seen[data]) {
                prev.next = current.next;
            } else {
                seen[data] = true;
                prev = current;
            }
            current = current.next;
        }// 数据首次出现，标记并移动前驱指针,数据重复，删除当前节点
    }

    private static void printList(listNode node) {
        while (node != null) {
            System.out.print(node.data + " ");
            node = node.next;
        }
    }

    public static void main(String[] args) {
        listNode head = new listNode(0);
        listNode b = new listNode(1);
        listNode c = new listNode(1);
        listNode d = new listNode(1);
        listNode e = new listNode(2);
        listNode f = new listNode(2);
        listNode g = new listNode(2);
        listNode h = new listNode(3);
        listNode i = new listNode(3);
        listNode j = new listNode(3);
        listNode k = new listNode(4);
        listNode l = new listNode(4);
        listNode m = new listNode(5);
        listNode n = new listNode(5);
        head.next = b;
        b.next = c;
        c.next = d;
        d.next = e;
        e.next = f;
        f.next = g;
        g.next = h;
        h.next = i;
        i.next = j;
        j.next = k;
        k.next = l;
        l.next = m;
        m.next = n;
        n.next = null;

        System.out.println("去重前");
        printList(head);

        removeDuplicates(head);
        System.out.println("\n去重后");
        printList(head);
    }


}



