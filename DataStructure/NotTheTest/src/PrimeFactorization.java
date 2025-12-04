import java.util.Scanner;

import static java.lang.Math.sqrt;

public class PrimeFactorization {
    static void main() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Int put two number to get the max");
        int end = sc.nextInt();
        sc.close();
        if (is(end) == 1) {
            System.out.println(end);
        }//质数则不需要后续计算
        else {
            int temp = end;

            while (temp % 2 == 0) {
                System.out.print("2" + " ");
                temp /= 2;
            }
            for (int i = 3; i * i <= temp; i += 2) {
                while (temp % i == 0) {
                    System.out.print(i + " ");
                    temp /= i;
                }
            }//非质数进行计算
            if (temp > 1) {
                System.out.println(temp);
            }//保证剩余的输出
        }
    }

    public static int is(int a) {
        int i;
        if (a <= 1)
            return 0;
        if (a == 2)
            return 1;
        if (a % 2 == 0)
            return 0;
        for (i = 3; i <= sqrt(a); i += 2) {
            if (a % i == 0)
                return 0;
        }
        return 1;
    }//判断是否为质数,为质数则最大公因数为其本身
}//赵润柯-质因数分解-时间复杂度O(sqrt(n))
