import java.util.Arrays;
import java.util.Scanner;

public class RingShiftLeft {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] show = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int n = show.length;
        System.out.println(Arrays.toString(show));//展示原有数组
        System.out.println("输入一个正整数");


        int k = sc.nextInt();
        if (k<0){
            System.out.println("淦,别搞");
        }
        k = k % 9;//避免超量翻转
        sc.close();



      reverse(show,0,n-1);
      reverse(show,0,k-1);
      reverse(show,k,n-1);
        System.out.println(Arrays.toString(show));

    }

    public static void reverse(int[] arr, int start, int end) {
        int temp;
        while (start < end) {
            temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++;
            end--;
        }
    }
}//高千智-右移n-时间复杂度O(n)空间O(1)
