import java.util.Arrays;
import java.util.Random;

// 1. 构建随机数组，长度为10
class RandomArrayGenerator {
    public int[] generateRandomArray(int length) {
        Random random = new Random();
        int[] arr = new int[length];
        for (int i = 0; i < length; i++) {
            arr[i] = random.nextInt(100); // 0-99的随机数
        }
        return arr;
    }
}

// 工具类
class HeapUtils {
    // 堆调整
    public static void heapify(int[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && arr[left] > arr[largest]) {
            largest = left;
        }

        if (right < n && arr[right] > arr[largest]) {
            largest = right;
        }

        if (largest != i) {
            swap(arr, i, largest);
            heapify(arr, n, largest);
        }
    }

    // 交换
    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}

// 2. 构建大堆
class MaxHeapBuilder {
    public void buildMaxHeap(int[] arr) {
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
            HeapUtils.heapify(arr, n, i); // 复用工具类的方法
        }
    }
}

// 3. 循环调用大堆构建，得到排序
class HeapSorter {
    public void heapSort(int[] arr) {
        int n = arr.length;

        MaxHeapBuilder builder = new MaxHeapBuilder();
        builder.buildMaxHeap(arr);

        for (int i = n - 1; i > 0; i--) {
            HeapUtils.swap(arr, 0, i);

            for (int j = i / 2 - 1; j >= 0; j--) {
                HeapUtils.heapify(arr, i, j);
            }
        }
    }
}

// 4. 主程序
public class HeapSort {
    public static void main(String[] args) {
        // 创建随机数组
        RandomArrayGenerator generator = new RandomArrayGenerator();
        int[] arr = generator.generateRandomArray(10);

        System.out.println("原始数组: " + Arrays.toString(arr));

        // 创建堆排序器
        HeapSorter sorter = new HeapSorter();

        // 执行堆排序
        sorter.heapSort(arr);

        System.out.println("排序后数组: " + Arrays.toString(arr));
    }
}