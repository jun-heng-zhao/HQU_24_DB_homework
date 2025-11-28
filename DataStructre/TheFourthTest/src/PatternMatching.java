
import java.util.ArrayList;
import java.util.List;

public class PatternMatching {


    public static List<Integer> kmp(String text, String pattern) {
        List<Integer> result = new ArrayList<>();
        if (pattern == null || pattern.isEmpty()) {
            return result;
        }

        int[] next = buildNext(pattern);
        int n = text.length();
        int m = pattern.length();
        int i = 0, j = 0;

        while (i < n) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            } else {
                if (j > 0) {
                    j = next[j - 1];
                } else {
                    i++;
                }
            }

            if (j == m) {
                result.add(i - j);
                j = next[j - 1];
            }
        }

        return result;
    }//KMP算法


    public static int[] buildNext(String pattern) {
        int m = pattern.length();
        int[] next = new int[m];

        if (m > 0) {
            next[0] = 0;
            int i = 1, j = 0;

            while (i < m) {
                if (pattern.charAt(i) == pattern.charAt(j)) {
                    j++;
                    next[i] = j;
                    i++;
                } else {
                    if (j != 0) {
                        j = next[j - 1];
                    } else {
                        next[i] = 0;
                        i++;
                    }
                }
            }
        }

        return next;
    }//构建next数组


    public static List<Integer> bruteForceSearch(String text, String pattern) {
        List<Integer> result = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        if (m == 0) {
            return result;
        }

        for (int i = 0; i <= n - m; i++) {
            boolean match = true;
            for (int j = 0; j < m; j++) {
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    match = false;
                    break;
                }
            }
            if (match) {
                result.add(i);
            }
        }

        return result;
    }//暴力匹配


    public static void main(String[] args) {
        String[][] testCases = {
                {"ABABDABACDABABCABAB", "ABABCABAB"},
                {"hello world", "ll"},
                {"aaaaa", "aa"},
                {"abcabcabc", "abc"},
                {"mississippi", "issip"},
                {"abcdefg", "xyz"}
        };

        System.out.println("模式匹配算法");


        for (String[] testCase : testCases) {
            String text = testCase[0];
            String pattern = testCase[1];

            System.out.println("\n文本: '" + text + "'");
            System.out.println("模式: '" + pattern + "'");

            // KMP算法
            long startTime = System.nanoTime();
            List<Integer> kmpPositions = kmp(text, pattern);
            long kmpTime = System.nanoTime() - startTime;

            // 暴力算法
            startTime = System.nanoTime();
            List<Integer> brutePositions = bruteForceSearch(text, pattern);
            long bruteTime = System.nanoTime() - startTime;

            // 输出结果
            System.out.println("KMP匹配位置: " + kmpPositions + " (耗时: " + kmpTime + " ns)");
            System.out.println("暴力匹配位置: " + brutePositions + " (耗时: " + bruteTime + " ns)");

        }//测试方法
    }
}

