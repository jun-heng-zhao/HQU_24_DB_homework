import java.util.Arrays;
import java.util.Random;

public class FastSort {

    // 主程序
    public static void main(String[] args) {
        FastSort fastSort = new FastSort();
        int[] arr = generateRandomArray(20, 0, 99);

        System.out.println("原始数组: " + Arrays.toString(arr));

        // 利用递归调用快速排序算法
        quickSortRecursive(arr, 0, arr.length - 1, fastSort);

        System.out.println("排序后的数组: " + Arrays.toString(arr));

    }

    // 快速排序主算法
    public int partition(int[] a, int First, int Right) {
        int pivot = a[First];
        int left = First + 1;
        int right = Right;

        while (left <= right) {

            while (left <= right && a[left] < pivot) {
                left++;
            }
            // 找到右边第一个小于等于基准的元素
            while (left <= right && a[right] > pivot) {
                right--;
            }

            if (left <= right) {
                // 交换这两个元素
                int temp = a[left];
                a[left] = a[right];
                a[right] = temp;
                left++;  // 移动左指针
                right--; // 移动右指针
            }
        }

        // 将基准元素放到其正确的位置
        a[First] = a[right];
        a[right] = pivot;

        return right; // 返回基准元素的最终位置
    }

    // 生成随机数组算法
    public static int[] generateRandomArray(int size, int min, int max) {
        Random random = new Random();
        int[] array = new int[size];

        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(max - min + 1) + min;
        }

        return array;
    }

    // 递归调用的快速排序算法
    private static void quickSortRecursive(int[] arr, int left, int right, FastSort fastSort) {
        if (left < right) {
            // 使用快速排序主算法进行分区
            int pivotIndex = fastSort.partition(arr, left, right);

            // 递归调用排序左子数组
            quickSortRecursive(arr, left, pivotIndex - 1, fastSort);

            // 递归调用排序右子数组
            quickSortRecursive(arr, pivotIndex + 1, right, fastSort);
        }
    }
}

