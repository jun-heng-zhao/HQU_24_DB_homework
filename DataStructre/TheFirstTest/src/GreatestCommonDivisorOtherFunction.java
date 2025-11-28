import java.util.Scanner;
public class GreatestCommonDivisorOtherFunction {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("输入两个数,要求同号");
        int a = sc.nextInt();
        int b = sc.nextInt();
        a = Math.abs(a);
        b = Math.abs(b);
        sc.close();
        int c;
        if (a == 0 && b == 0) {
            System.out.println("输入全为0");
            return;
        }
        if (a == 0) {
            System.out.println("输入第一个为0");
            return;
        }
        if (b == 0) {
            System.out.println("输入第二个为0");
            return;
        }
        while (b != 0) {
            c = a % b;
            a = b;
            b = c;
        }
        System.out.println(a);
    }
}//赵润柯-辗转相除法-时间复杂度O()不知道