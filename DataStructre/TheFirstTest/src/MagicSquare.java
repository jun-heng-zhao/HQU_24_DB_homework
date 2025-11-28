import java.util.Scanner;
public class MagicSquare {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a number: ");
        int number = scanner.nextInt();
        int n = 1;
        int x = 0;
        int y = number / 2;
        scanner.close();
        int[][] a = new int[number][number];
        while (n <= number * number) {
            a[x][y] = n;
            int newX = x - 1;
            int newY = y + 1;
            if (newX < 0) {
                newX = newX + number;
            }
            if (newY == number) {
                newY = 0;
            }
            if (a[newX][newY] == 0) {
                x = newX;
                y = newY;
            } else {
                x = x + 1;
                if (x == number) {
                    x = 0;
                }
            }
            n++;
        }
        for (int i = 0; i < number; i++) {
            for (int j = 0; j < number; j++) {
                System.out.print(a[i][j] + "\t");
            }
            System.out.println();
        }
    }
}//赵润柯-奇数阶幻方填写-时间复杂度-O(n)



