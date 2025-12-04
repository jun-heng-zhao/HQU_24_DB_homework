import java.util.Scanner;

public class PrimeRing {
    public static class node {
        int data;
        node next;

        node() {
            this.data = -1;
            this.next = null;
        }

        node(int data) {
            this.data = data;
            this.next = null;
        }
    }// 定义队列节点

    public boolean isEmpty() {
        return size == 0;  // 当元素数量为0时，队列为空
    }

    private node head;
    private node tail;
    int size;

    public node getRing(int began, int end) {
        for (int i = began; i < end; i++) {
            node p = new node(i);
            if (head == null) {
                head = p;
            } else {
                tail.next = p;
            }
            tail = p;
            size++;
        }
        return head;
    }// 构建队列

    public int next() {
        if (isEmpty()) {
            System.out.println("队列为空");
            return -1;
        }
        node temp = head;
        int data = temp.data;
        head = head.next;
        size--;
        if (isEmpty()) {
            tail = null;
        }
        return data;
    }// 出队并返回队头元素

    public void enqueue(int data) {
        node newNode = new node(data);
        if (isEmpty()) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
        size++;
    }// 将元素入队到队尾

    public boolean isPrime(int num) {
        if (num <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }// 判断一个数是否为素数

    public static void main(String[] args) {
        Scanner SC = new Scanner(System.in);
        System.out.println("输入队列的头和尾");
        int began = SC.nextInt();
        int end = SC.nextInt();
        SC.close();

        PrimeRing PR = new PrimeRing();

        PR.getRing(began, end);
        int size = end - began;  // 队列中元素数量：end - began
        if (size <= 0) {
            System.out.println("不成立");
            return;
        }
        int[] arr = new int[size];

        // 第一个元素直接出队放入数组
        arr[0] = PR.next();
        int index = 1;

        while (index < size) {
            int currentQueueSize = PR.size;  // 记录当前队列长度，避免无限循环
            int tryCount = 0;
            boolean found = false;

            // 尝试当前队列中所有元素，若都不满足则无法形成
            while (tryCount < currentQueueSize) {
                if (PR.isEmpty()) {
                    break;
                }
                int current = PR.next();
                tryCount++;

                // 检查与数组最后一个元素的和是否为素数
                if (PR.isPrime(arr[index - 1] + current)) {
                    arr[index] = current;
                    index++;
                    found = true;
                    break;
                } else {
                    // 不满足则放回队尾
                    PR.enqueue(current);
                }
            }

            // 若当前队列中所有元素都尝试过仍未找到合适的，说明无法形成
            if (!found) {
                System.out.println("不成立");
                return;
            }
        }

        // 数组填满且相邻元素和均为素数，直接输出
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }
}