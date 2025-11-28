import java.util.Scanner;//最小未出现整数

public class TheMinUnfundedInteger {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入数组长度：");
        int length = sc.nextInt();
        int[] arr = new int[length];

        System.out.println("请输入数组元素（0~100之间）：");
        for (int i = 0; i < arr.length; i++) {
            arr[i] = sc.nextInt();
            // 确保输入在指定范围内
            if (arr[i] < 0 || arr[i] > 100) {
                System.out.println("警告：输入超出0~100范围，将被忽略");
                arr[i] = -1; // 标记为无效值
            }
        }
        sc.close();

        // 利用固定范围特性：创建0~100的计数数组
        int[] counts = new int[101]; // 索引0~100对应数字0~100

        // 统计数字出现次数
        for (int num : arr) {
            if (num != -1) { // 只处理有效数字
                counts[num]++;
            }
        }

        // 查找最小的未出现正整数
        int smallestMissing = 1; // 从正整数1开始检查
        while (smallestMissing <= 100) {
            if (counts[smallestMissing] == 0) {
                break; // 找到第一个未出现的正整数
            }
            smallestMissing++;
        }

        System.out.println("\n最小的未出现正整数是：" + smallestMissing);
    }
}
