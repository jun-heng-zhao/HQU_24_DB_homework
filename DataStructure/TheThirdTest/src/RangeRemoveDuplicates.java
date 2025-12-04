import java.util.Random;
//去重，模式串随机生成
public class RangeRemoveDuplicates {
    public static class listNode {
        int data;
        listNode next;

        public listNode(int a) {
            this.data = a;
            this.next = null;
        }
    }

    public static listNode createRandomLinkedList(int length, int min, int max) {
        if (length <= 0) {
            return null;
        }

        Random random = new Random();
        listNode head = new listNode(random.nextInt(max - min + 1) + min);
        listNode current = head;

        for (int i = 1; i < length; i++) {
            int randomData = random.nextInt(max - min + 1) + min;
            listNode newNode = new listNode(randomData);
            current.next = newNode;
            current = newNode;
        }
        return head;
    }//创建随机链表

    public static void removeDuplicates(listNode head) {
        if (head == null) {
            return; // 空链表无需处理
        }

        boolean[] seen = new boolean[101];
        seen[head.data] = true;

        listNode prev = head;
        listNode current = head.next;

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
    }//去重

    public static void printLinkedList(listNode head) {
        if (head == null) {
            System.out.println("链表为空");
            return;
        }

        listNode current = head;
        System.out.print("去重后的链表：");
        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }
        System.out.println("null");
    } //打印链表（完整输出）

    public static void main(String[] args) {

        listNode head = createRandomLinkedList(20, 1, 10); //创建链表
        printLinkedList(head);
        removeDuplicates(head);

        System.out.println();
        printLinkedList(head); //输出去重结果
    }
}
