import java.util.ArrayList;
import java.util.List;

public class KMP {

    public static List<Integer> kmpSearch(String text, String pattern) {
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
    }  //KMP算法

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
    }//next数组

    public static void main(String[] args) {
        String[][] testCases = {
                {"ABABDABACDABABCABAB", "ABABCABAB"},
                {"hello world", "ll"},
                {"aaaanüfhrth4hbdffaaaudfda", "aa"},
                {"abcabcabc", "abc"},
                {"mississippi", "issip"},
                {"abcdefg", "xyz"}
        };

        for (String[] testCase : testCases) {
            String text = testCase[0];
            String pattern = testCase[1];

            System.out.println("文本: '" + text + "'");
            System.out.println("模式: '" + pattern + "'");

            List<Integer> positions = kmpSearch(text, pattern);
            System.out.println("匹配位置: " + positions);
            System.out.println("匹配次数 "+positions.size());
            System.out.println("------------------------");
        }
    }
}