import java.util.Scanner;

public class LongIntMultiplication {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("输入一个长整数");
        long a = sc.nextInt();
        System.out.println("输入一个长整数");
        long b = sc.nextInt();
        sc.close();
        long end = getend(Math.toIntExact(a), Math.toIntExact(b));
        if (a * b > 0) {
            System.out.println(end);
        }
        if (a * b < 0) {
            System.out.println(-end);
        }

    }

    public static long getend(int a, int b) {
        int absA = Math.abs(a);
        int absB = Math.abs(b);

        String numStrA = Integer.toString(absA);
        String numStrB = Integer.toString(absB);

        int[] digitsArrayA = new int[numStrA.length()];
        int[] digitsArrayB = new int[numStrB.length()];


        for (int i = 0; i < numStrA.length(); i++) {
            digitsArrayA[i] = numStrA.charAt(i) - '0';
        }
        for (int i = 0; i < numStrB.length(); i++) {
            digitsArrayB[i] = numStrB.charAt(i) - '0';
        }//AB存入数组

        int lenA = digitsArrayA.length;
        int lenB = digitsArrayB.length;
        int[] result = new int[lenA + lenB];

        for (int i = lenA - 1; i >= 0; i--) {
            for (int j = lenB - 1; j >= 0; j--) {
                int mul = digitsArrayA[i] * digitsArrayB[j];//每位乘积
                int pos = i + j + 1;
                int sum = mul + result[pos];
                result[pos] = sum % 10; // 保留当前位
                result[pos - 1] += sum / 10; // 进位到前一位
            }
        }

        long product = 0;
        for (int num : result) {
            if (product == 0 && num == 0) {
                continue;
            }
            product = product * 10 + num;

        }
        return product;
    }
}
