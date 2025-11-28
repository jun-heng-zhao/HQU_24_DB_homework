import java.util.Scanner;

public class SnakeArr {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int length = sc.nextInt();
        int rowCount = 2 * length - 1;
        int[][] arr = new int[rowCount][];


        for (int i = 0; i < rowCount; i++) {
            if (i < length) {
                arr[i] = new int[i + 1];
            } else {
                arr[i] = new int[2 * length - 1 - i];
            }
        }//设置每行的列数


        int currentNum = 1;
        for (int i = 0; i < rowCount; i++) {
            int cols = arr[i].length;
            if (i % 2 == 0) {

                for (int j = 0; j < cols; j++) {
                    arr[i][j] = currentNum++;
                }// 偶数行（0,2,4,6）：从左到右填充
            } else {
                for (int j = cols - 1; j >= 0; j--) {
                    arr[i][j] = currentNum++;
                }
            }// 奇数行（1,3,5）：从右到左填充
        }

        int[][] flippedArr = new int[length][length];
        for (int i = 0; i < rowCount; i++){
            int cols = arr[i].length;
            for (int j = 0; j < cols; j++) {
                int x, y;
                if (i < length) {
                    x = i - j;
                    y = j;
                } else {
                    x = (length - 1) - j;
                    y = (i - (length - 1)) + j;
                }
                flippedArr[x][y] = arr[i][j];
            }
        }// 赋值到新数组
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                System.out.print(flippedArr[i][j] + "\t");
            }
            System.out.println();
        }//输出
        }



}