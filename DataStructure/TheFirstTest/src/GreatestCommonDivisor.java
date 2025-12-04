import java.util.Scanner;

public class GreatestCommonDivisor {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("");
        int a = sc.nextInt();
        int b = sc.nextInt();
        sc.close();
        if (a == 0 && b == 0) {
            System.out.println("无");
            return;
        }
        if (a == 0) {
            System.out.println("第一个数为0最大公因数为" + b);
            return;
        }
        if (b == 0) {
            System.out.println("第二个数为0,最大公因数为" + a);
            return;
        }//保证a/b任意一个不是0

        if (a == b) {
            System.out.println("两数相同,最大公约数为本身" + a);
            return;
        }//保证不相同

        int c;

        int count = 0;
        while (true) {
            if (a < b) {
                c = a;
                a = b;
                b = c;
            }//保证a>b
            if (a % 2 == 0 && b % 2 == 0) {
                a = a / 2;
                b = b / 2;
                count++;
            }//对于2的倍数先化简到最小
            c = a - b;
            if (c == 0) {
                System.out.println(b * (1 << count));
                break;
            }
            a = b;
            b = c;
        }

    }
}//赵润柯-更相减损法-时间复杂度O()???
