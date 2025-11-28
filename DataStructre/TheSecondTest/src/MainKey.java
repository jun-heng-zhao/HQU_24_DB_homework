import java.util.Scanner;//求主键，但数组要自己输
public class MainKey {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Length");
        int a = sc.nextInt();
        int[] arr = new int[a];
        System.out.println(" As you want");
        for (int i = 0; i < arr.length; i++) {
            arr[i] = sc.nextInt();
        }
        sc.close();
        int MainKey = arr[0];
        int count = 1;
        for (int i = 1; i < arr.length; i++) {

            if (arr[i] == MainKey) {
                count++;
            } else {
                count--;
                if (count == 0) {
                    MainKey = arr[i];
                    count = 1;
                }
            }
        }
        int counts = YON(arr, MainKey);
        if (counts < arr.length / 2) {
            System.out.println("None");
        } else {
            System.out.println(MainKey);
        }
    }

    public static int YON(int[] a, int MainKey) {
        int count = 0;
        for (int j : a) {
            if (j == MainKey) {
                count++;
            }
        }
        return count;

    }

}