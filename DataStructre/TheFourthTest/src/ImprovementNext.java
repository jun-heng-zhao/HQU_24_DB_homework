public class ImprovementNext {

    public static int[] buildNextImproved(String pattern) {
        int m = pattern.length();
        int[] next = new int[m + 1]; // 长度增加1，便于处理边界

        if (m > 0) {
            next[0] = -1; // 改进点1: 使用-1作为起始值
            int i = 0, j = -1;

            while (i < m) {
                if (j == -1 || pattern.charAt(i) == pattern.charAt(j)) {
                    i++;
                    j++;
                    // 改进点2: 优化相同字符的next值
                    if (i < m && pattern.charAt(i) == pattern.charAt(j)) {
                        next[i] = next[j];
                    } else {
                        next[i] = j;
                    }
                } else {
                    j = next[j];
                }
            }
        }

        return next;
    }//改进的next数组

    public static void printArray(int[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }//打印数组

    public static void main(String[] args) {
        String[][] testCases = {
                {"ABABDABACDABABCABAB", "ABABCABAB"},
        };

        System.out.println("=== KMP算法改进版测试 ===");


        String pattern = testCases[0][1];
        System.out.println("模式: '" + pattern + "'");

        int[] improvedNext = buildNextImproved(pattern);
        System.out.print("改进的Next数组: ");
        printArray(improvedNext);
    }
}