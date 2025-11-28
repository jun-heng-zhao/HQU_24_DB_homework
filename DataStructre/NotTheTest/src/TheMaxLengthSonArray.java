import java.util.concurrent.ThreadLocalRandom;

public class TheMaxLengthSonArray {
    public static int[] GetRandom(int min, int max, int length) {
        int[] arr = new int[length];
        for (int i = 0; i < length; i++) {
            arr[i] = ThreadLocalRandom.current().nextInt(min, max + 1);
        }
        return arr;
    }//生成随机数组，包括负数

    public static void main(String[] args) {
        int Max;
        int CurrMax;
        int [] arr = GetRandom(-5,5,20);
        for (int j : arr) {
            System.out.print(j + " ");
        }
        CurrMax=arr[0];
        Max=arr[0];
        for (int i = 1; i < arr.length; i++) {
            CurrMax=Math.max(CurrMax+arr[i],arr[i]);
            Max=Math.max(Max,CurrMax);
        }
        System.out.println(" ");
        System.out.println(Max);
    }
}
