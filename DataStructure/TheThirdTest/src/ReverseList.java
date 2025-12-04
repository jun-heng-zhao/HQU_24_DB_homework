//反转链表

import java.util.Scanner;

public class ReverseList {
    public static class listNode {
        int data;
        listNode next;

        public listNode(int a) {
            this.data = a;
            this.next = null;
        }

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("long");
        int length = sc.nextInt();
        sc.close();

        listNode head = new listNode(110101);//设置头节点
        listNode tail = head;

        for (int i = 0; i < length; i++) {
            listNode TheNewOne = new listNode(i);
            tail.next = TheNewOne;
            tail = TheNewOne;
        }//通过tail指向每一个节点，为其赋初值，此时tail指向最后一个节点，

        listNode now = head;//保证有一个指向头节点的指针head

        while (now != null) {
            System.out.print(now.data + " ");
            now = now.next;
        }//循环输出now指针指向最后一个节点
        System.out.println(" ");

        listNode prev = null;
        now = head.next;
        listNode next;

        while (now != null) {
            next = now.next;
            now.next = prev;
            prev = now;
            now = next;
        }
        head.next = prev;
        now = head; // 从头节点开始遍历
        while (now != null) {
            System.out.print(now.data + " ");
            now = now.next;
        }
    }
}
