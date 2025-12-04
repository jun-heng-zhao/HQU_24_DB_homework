import java.util.Scanner;
public class YangHuiTriangle {
    public static void main(String[] args) {
        System.out.println("输入你需要几行");
        Scanner sc = new Scanner(System.in);// 获取输出多大的杨辉三角
        int length = sc.nextInt();
        int[][] a = new int[length][];
        sc.close();
        for (int i = 0; i < length; i++) {
            a[i] = new int[i + 1];
            a[i][0] = 1;
            a[i][i] = 1;
        } // 初始化杨辉三角的两边 复杂度O(n)
        for (int i = 2; i < length; i++) {
            for (int j = 1; j < i; j++) {
                a[i][j] = a[i - 1][j - 1] + a[i - 1][j];
            }
        } // 循环获取杨辉三角内部各个数据 复杂度O(n^2)
        int max = 0;
        for (int i = 0; i < length; i++) {
            if (a[length - 1][i] > max) {
                max = a[length - 1][i];
            }
        } // 获取最大值,便于对齐
        int dig = String.valueOf(max).length() + 2;// 计算数字宽度
        for (int i = 0; i < length; i++) {
            int leading = (length - 1 - i) * dig / 2;
            for (int k = 0; k < leading; k++) {
                System.out.print(" ");
            } // 打印空格使居中
            for (int j = 0; j <= i; j++) {
                System.out.printf("%" + dig + "d", a[i][j]);
            }
            System.out.println();
        }
    }

}//赵润柯-杨辉三角-时间复杂度O(n)

